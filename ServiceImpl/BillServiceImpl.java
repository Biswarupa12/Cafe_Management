package com.cafe.cafe_management.ServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafe_management.Constants.CafeConstants;
import com.cafe.cafe_management.Filter.JwtFilter;
import com.cafe.cafe_management.Model.Bill;
import com.cafe.cafe_management.Repo.BillRepo;
import com.cafe.cafe_management.Service.BillService;
import com.cafe.cafe_management.utils.CafeUtils;
import com.google.gson.JsonArray;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillRepo billRepo;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport");
        
        try {
                String fileName;
                if(ValidateRequestMap(requestMap)){
                    if(requestMap.containsKey("isGenerate") && !(Boolean)requestMap.get("isGenerate")){
                        fileName = (String) requestMap.get("uuid");
                    }else{
                        fileName = CafeUtils.getUUiD();
                        requestMap.put("uuid",fileName);
                        insertBill(requestMap);
                    }

                    // String data = "Name: "+requestMap.get("name")+"\n" + "Contact Number:  "+requestMap.get("contactNumber")+ "\n" 
                    //                         + "Email: "+requestMap.get("email")+ "\n" + "Payment Method: "+requestMap.get("paymentMethod")+"\n" ;


                    //                         Document document = new Document();
                    //                         PdfWriter.getInstance(document, new OutputStream(CafeConstants.STORE_LOCATION +"\\"+fileName+".pdf"));

                    //                         document.open();
                    //                         setRectangleInPdf(document);

                    String data = "Name: " + requestMap.get("name") + "\n" + 
              "Contact Number: " + requestMap.get("contactNumber") + "\n" +
              "Email: " + requestMap.get("email") + "\n" + 
              "Payment Method: " + requestMap.get("paymentMethod") + "\n";

Document document = new Document();
try {
    String filePath = CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf";
    OutputStream outputStream = new FileOutputStream(filePath);
    PdfWriter.getInstance(document, outputStream);

    document.open();
    setRectangleInPdf(document);
    // Write your data to the document here
    Paragraph chunk = new Paragraph("Cafe Management System" ,getFont("Header"));      //your resturant name 
    chunk.setAlignment(Element.ALIGN_CENTER);
    document.add(chunk);

    Paragraph paragraph = new Paragraph(data+"\n \n" , getFont("Data"));
    document.add(paragraph);


    PdfPTable table = new PdfPTable(5);

    table.setWidthPercentage(100);
    addTableHeader(table);

    JsonArray jsonArray = CafeUtils.getJsonArrayFromString((String)requestMap.get("productDetails"));

    for(int i = 0;i < jsonArray.size() ; i++){
        addRows(table,CafeUtils.getMapfromJson(jsonArray.getAsString())); // i is the parameter..

    }
    document.add(table);
    Paragraph footer =  new Paragraph("Total :"+requestMap.get("totalamount")+"\n"
        +"Thank you  ... Please Visit again !!",getFont("Data"));
        document.add(footer);


    document.close();
    return new ResponseEntity<>("{\"uuid\":\""+fileName +"\"}", HttpStatus.OK);
    }
    catch (FileNotFoundException e) {
    e.printStackTrace();
} catch (DocumentException e) {
    e.printStackTrace();
}
} 
                return CafeUtils.getResponseEntity("Required data Not found ", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));

    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorder(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;

            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;

            default:
                return new Font();
        }

    }

    private void setRectangleInPdf(Document document) throws DocumentException {

        log.info("Inside setRectangleInPdf");
        Rectangle rect = new Rectangle(557, 825, 18, 15);

        rect.enableBorderSide(1); // left side of rectangle
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1.1f);
        document.add(rect);

    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("payment method"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setProductDetails((String) requestMap.get("product details"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billRepo.save(bill);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateRequestMap(Map<String, Object> requestMap) {

        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productdetails") &&
                requestMap.containsKey("totlAmount");

    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
       
        List<Bill> list = new ArrayList<>();
        if(jwtFilter.isAdmin()){
                list = billRepo.getAllBills();
        }
        else{
                list = billRepo.getBillByUserName(jwtFilter.getCurrentUser());
        }


        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
      
        log.info("Inside getPdf : requestMap{}" , requestMap);
        try {
                byte[] byteArray = new byte[0];
                if(!requestMap.containsKey("uuid") && ValidateRequestMap(requestMap))
                    return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
                String filePath = CafeConstants.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";

                if(CafeUtils.isFileExist(filePath)){
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray , HttpStatus.OK);
                }else{
                    requestMap.put("isGenerate" ,false);
                    generateReport(requestMap);
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray,HttpStatus.OK);
                }







        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }

    private byte[] getByteArray(String filePath) throws IOException {
        File initialFile = new File(filePath);
        InputStream targStream =  new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targStream);
        targStream.close();
        return byteArray;


}

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
       
        try {
            Optional optional = billRepo.findById(id);
            if(!optional.isEmpty()){
                billRepo.deleteById(id);
                return CafeUtils.getResponseEntity("Bill deleted successfully", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Bill id doesnot exist", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
