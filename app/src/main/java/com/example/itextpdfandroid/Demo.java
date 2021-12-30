package com.example.itextpdfandroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Demo extends AppCompatActivity {


    private void createPdf() throws FileNotFoundException {
        String pdfPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();

        File file = new File(pdfPath,"WaterTicket.pdf");
        OutputStream outputStream = new FileOutputStream(file);


        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);


//        document.add(createParagraph());
//        document.add(createList());
//        document.add(createImage());
//        document.add(createTable());
//        document.add(createBarcode(pdfDocument));

        document.close();
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show();

//        try {
//            imageView.setImageBitmap(generatePDFBitmap(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    Image createBarcode(PdfDocument pdfDocument){
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("mailto:info@xyz.com?"
                +"subject=Frogora%20Hacker&"
                +"body=Hello Friends , i am hacker");
        PdfFormXObject pdfFormXObject = barcodeQRCode.createFormXObject(ColorConstants.LIGHT_GRAY,pdfDocument);
        Image barcodeImage = new Image(pdfFormXObject).setWidth(300f).setHeight(300f);
        return barcodeImage;
    }


    Table createTable(){
        Border border = new GrooveBorder(5);
        float columnWidth[] = {200f,200f};
        Table table = new Table(columnWidth);
        table.addCell("Name");
        table.addCell("Age");

        table.addCell(new Cell(2,1)
                .setBackgroundColor(ColorConstants.GREEN)
                .add(new Paragraph("Raja Ram ")
                        .setBorder(border)
                ));
        table.addCell("32");
//        table.addCell("Sumit");
        table.addCell("21");
        table.setBorder(border);
        return table;
    }

    Image createImage(){
        Drawable d = getDrawable(R.drawable.yoda);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        return  image;
    }



    List createList(){
        Drawable blazeDrawable = getDrawable(R.drawable.blaze);
        Bitmap blazeBitmap = ((BitmapDrawable)blazeDrawable).getBitmap();
        ByteArrayOutputStream blazeStream = new ByteArrayOutputStream();
        blazeBitmap.compress(Bitmap.CompressFormat.PNG,100,blazeStream);
        byte[] blazeStreamBitmapData = blazeStream.toByteArray();

        ImageData blazeImageData = ImageDataFactory.create(blazeStreamBitmapData);
        Image blazeImage = new Image(blazeImageData);
        blazeImage.setWidth(16);
        blazeImage.setHeight(16);
        List list = new List();
//        list.setListSymbol("\u00A5 ");
        list.setListSymbol(blazeImage);
        list.add("ANdroid");
        list.add("Java");
        list.add("C++");
        list.add("Kotline");
        return list;
    }

    Paragraph createParagraph(){
        Paragraph paragraph = new Paragraph("Hello This is my first pdf");

        Text text1 = new Text("Bold").setBold();
        Text text2 = new Text("Italic").setItalic();
        Text text3 = new Text("Underline").setUnderline();
        Text text4 = new Text("Mixed").setBold().setItalic().setUnderline();
        paragraph.add("\n");
        paragraph.add(text1)
                .add(text2)
                .add(text3)
                .add(text4);
        return  paragraph;
    }

}
