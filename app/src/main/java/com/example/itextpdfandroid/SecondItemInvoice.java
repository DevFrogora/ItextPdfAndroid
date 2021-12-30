package com.example.itextpdfandroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SecondItemInvoice extends AppCompatActivity {

    class ProductItem{
        int id;
        String  name;
        int qty;
        int cost;
        int totalAmount;
    }

    HashMap<String, Table> createTable(HashMap<String, Object> invoiceData , HashMap<String, Object> itemsData ,
                                       HashMap<String, Object> contactsData, HashMap<String, Object> customerData ,
                                       HashMap<String, Object> paymentData ) {

        float columnWidth[] = {140, 140, 140, 140};
        Table table = new Table(columnWidth);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        Image logoWaterImage = createImage( getDrawable(R.drawable.logo_water));
        logoWaterImage.setWidth(100f);

        //Table1 ------ Row 1
        table.addCell(new Cell(4,1).add(logoWaterImage).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell(1,2).add(new Paragraph("Invoice")
                .setFontSize(26f).setFontColor(ColorConstants.GREEN)).setBorder(Border.NO_BORDER));
//        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Table1 ------Row 2
//        table.addCell(new Cell().add(new Paragraph()));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Invoice No:")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(invoiceData.get("invoiceNumber").toString())).setBorder(Border.NO_BORDER));

        //Table1 ------Row 3
//        table.addCell(new Cell().add(new Paragraph()));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Invoice Date:")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(invoiceData.get("invoiceDate").toString())).setBorder(Border.NO_BORDER));

        //Table1 ------Row 4
//        table.addCell(new Cell().add(new Paragraph()));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Account No:")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(invoiceData.get("accountNumber").toString())).setBorder(Border.NO_BORDER));

        //Table1 ------Row 5
        table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Table1 ------Row 6
        table.addCell(new Cell().add(new Paragraph("To")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Table1 ------Row 7
        table.addCell(new Cell().add(new Paragraph(customerData.get("name").toString())).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Payment Method")
                .setBold()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Table1 ------Row 8
        table.addCell(new Cell().add(new Paragraph(customerData.get("village").toString())).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell(1,2).add(new Paragraph(paymentData.get("userid").toString())).setBorder(Border.NO_BORDER));
//        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Table1 ------Row 9
        table.addCell(new Cell().add(new Paragraph(customerData.get("city").toString())).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell(1,2).add(new Paragraph("Card Payment: We accept "+paymentData.get("cardAccept").toString())).setBorder(Border.NO_BORDER));
//        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));



        float columnWidth2[] ={62,162,112,112,112};
        Table table2 = new Table(columnWidth2);
//        table2.setHorizontalAlignment(HorizontalAlignment.CENTER);

        ProductItem[] productItems = (ProductItem[]) itemsData.get("items");
        ProductItem productItem1 = productItems[0];
        ProductItem productItem2 = productItems[1];
        ProductItem productItem3 = productItems[2];
        ProductItem productItem4 = productItems[3];

        int subtotal = productItem1.totalAmount + productItem2.totalAmount
                +productItem3.totalAmount + productItem4.totalAmount;

        int discount = 0;

        int taxrate = 12;
        int tax = (subtotal/100)*taxrate;

        int invoiceTotal = subtotal+tax;

        //Table2 ------Row 1
        table2.addCell(new Cell().add(new Paragraph("S.No")
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("ITEM DESCRIPTIONS")
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("RATE")
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("QTY")
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("Price")
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));

        //Table2 ------Row 2
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.id)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(productItem1.name))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.cost)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.qty)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.totalAmount)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));

        //Table2 ------Row 3
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.id)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(productItem2.name))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.cost)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.qty)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.totalAmount)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));

        //Table2 ------Row 4
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.id)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(productItem3.name))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.cost)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.qty)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.totalAmount)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));

        //Table2 ------Row 5
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.id)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(productItem4.name))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.cost)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.qty)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.totalAmount)))
                .setBackgroundColor(ColorConstants.GRAY).setBorder(Border.NO_BORDER));

        //Table2 ------Row 6
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("Sub-Total")
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(subtotal))
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));

        //Table2 ------Row 7
        table2.addCell(new Cell(1,2).add(new Paragraph("Terms & Conditions:")
                .setBold()).setBorder(Border.NO_BORDER));
//        table2.addCell(new Cell().add(new Paragraph()));
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("GST("+taxrate+"%)")
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(tax))
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));

        //Table2 ------Row 8
        table2.addCell(new Cell(1,2).add(new Paragraph("Goods sold are not returnable and exchangable")).setBorder(Border.NO_BORDER));
//        table2.addCell(new Cell().add(new Paragraph()));
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("Grand Total")
                .setBold().setFontSize(16)
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(invoiceTotal))
                .setBold().setFontSize(16)
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));

        float columnWidth3[] = {50,250,260};
        Table table3 = new Table(columnWidth3);

        Image contactImage = createImage( getDrawable(R.drawable.contact_list_image));
        Image thanksImage = createImage( getDrawable(R.drawable.thanks_image));

        contactImage.setHeight(120);
        thanksImage.setHeight(120);
        thanksImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        table3.addCell(new Cell(3,1).add(contactImage).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph(contactsData.get("email").toString())).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell(3,1).add(thanksImage).setBorder(Border.NO_BORDER));

//        table3.addCell(new Cell().add());
        table3.addCell(new Cell().add(new Paragraph(contactsData.get("phone").toString())).setBorder(Border.NO_BORDER));
//        table3.addCell(new Cell().add(new Paragraph()));

//        table3.addCell(new Cell(3,1).add(contactImage));
        table3.addCell(new Cell().add(new Paragraph(contactsData.get("location").toString())).setBorder(Border.NO_BORDER));
//        table3.addCell(new Cell().add(new Paragraph()));


        HashMap<String, Table> tables=new HashMap<>();
        tables.put("firstTable",table);
        tables.put("secondTable",table2);
        tables.put("thirdTable",table3);
        return tables;
    }

    Image createImage(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        return  image;
    }
}
