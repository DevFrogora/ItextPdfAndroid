package com.example.itextpdfandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

public class FirstItemInvoicePDF extends AppCompatActivity {

    EditText editTextName,editTextAge,editTextNumber,editTextLocation;
    Button submitButton;

    class ProductItem{
        String  name;
        int qty;
        int cost;
        int totalAmount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_layout);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextLocation = findViewById(R.id.editTextLocation);
        submitButton = findViewById(R.id.button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();
                String number = editTextNumber.getText().toString();
                String location = editTextLocation.getText().toString();

                try {

                    HashMap<String, Object> hashMapData = new HashMap<>();


                    // Adding values to HashMap as ("keys", "values")
                    hashMapData.put("fromLocation", "Mother Terisa Road\nGuwahati, Assam , India\n781026");
                    hashMapData.put("contacts", "995-499-7214\nfrogora@gmail.com\nfrogoraWorld.com");
                    hashMapData.put("toLocation", "Ramu Prasad\nShivaji Road\nMumbai, Maharastra, India\n310267");

                    DateTimeFormatter dateTimeFormatter = null, timeFormatter = null;
                    String time = "";
                    String issueDate = "";
                    String dueDate = "";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
                        issueDate = LocalDate.now().format(dateTimeFormatter).toString();
                        dueDate = LocalDate.now().plusDays(3).format(dateTimeFormatter).toString();
                        time = LocalTime.now().format(timeFormatter).toString();
                    }

                    hashMapData.put("issueDate", issueDate);
                    hashMapData.put("dueDate", dueDate);


                    ProductItem productItem1 = new ProductItem();
                    ProductItem productItem2 = new ProductItem();
                    ProductItem productItem3 = new ProductItem();
                    ProductItem productItem4 = new ProductItem();
                    ProductItem[] productItems = new ProductItem[]{productItem1, productItem2,
                            productItem3, productItem4};

                    productItem1.name = "Rice";
                    productItem1.qty = 1;
                    productItem1.cost = 100;
                    productItem1.totalAmount = productItem1.qty * productItem1.cost;

                    productItem2.name = "Wheat";
                    productItem2.qty = 1;
                    productItem2.cost = 100;
                    productItem2.totalAmount = productItem2.qty * productItem2.cost;

                    productItem3.name = "Onion";
                    productItem3.qty = 1;
                    productItem3.cost = 100;
                    productItem3.totalAmount = productItem3.qty * productItem3.cost;

                    productItem4.name = "Potato";
                    productItem4.qty = 1;
                    productItem4.cost = 100;
                    productItem4.totalAmount = productItem4.qty * productItem4.cost;
                    Random random1 = new Random();
                    int invoiceNumber = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        invoiceNumber = random1.ints(1, 158220, 208220).findFirst().getAsInt();
                        ;
//                       //https://stackoverflow.com/a/48658684/10111806
                    }
                    hashMapData.put("items", productItems);
                    hashMapData.put("invoiceNumber", invoiceNumber);

                    createPdf(hashMapData);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }



        private void createPdf(HashMap<String, Object> hashMapData ) throws FileNotFoundException {
        String pdfPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();

        File file = new File(pdfPath,"ItemInvoice.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);


        Table table = createTable(hashMapData);
        document.add(table);
        document.add(new Paragraph("\n\n\n(Authorised Signatory)"));

        document.close();
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show();

        try {

            Bitmap bmp = generatePDFBitmap(file);

            //Source : https://stackoverflow.com/questions/11010386/passing-android-bitmap-data-within-activity-using-intent-in-android

            //Write file
            String filename = "invoiceItem.png";
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
            bmp.recycle();

            //Pop intent
            Intent in1 = new Intent(this, PdfPreviewActivity.class);
            in1.putExtra("image", filename);
            startActivity(in1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Table createTable(HashMap<String, Object> hashMapData ){

        float columnWidth[] = {140,112,112,112,112};
        Table table = new Table(columnWidth);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //Row 1
        table.addCell(new Cell(1,2).add(new Paragraph("Water Supply")
                .setFontSize(20f).setBold().setFontColor(ColorConstants.BLUE)
        ).setBorder(Border.NO_BORDER));
//        table.addCell(new Cell().add(new Paragraph()));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Row 2
        table.addCell(new Cell().add(new Paragraph(hashMapData.get("fromLocation").toString())).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(hashMapData.get("contacts").toString())).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Row 3
        table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Row 4
        Text textBilledTo = new Text("BILLED TO \n");
//
        textBilledTo.setBold();
        textBilledTo.setFontColor(ColorConstants.GREEN);

        Paragraph paragraphBilledTO = new Paragraph();
        paragraphBilledTO.add(textBilledTo);
        paragraphBilledTO.add(hashMapData.get("toLocation").toString());

        table.addCell(new Cell().add(paragraphBilledTO).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Row 5
        table.addCell(new Cell(2,1).add(new Paragraph("Invoice").setBold()
                .setFontSize(24).setFontColor(ColorConstants.GREEN)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Row 6
//        table.addCell(new Cell().add(new Paragraph()));
        table.addCell(new Cell().add(new Paragraph("Description")
        ).setFontColor(ColorConstants.WHITE).setBackgroundColor(ColorConstants.BLUE));
        table.addCell(new Cell().add(new Paragraph("UNIT COST(INR)")
        ).setFontColor(ColorConstants.WHITE).setBackgroundColor(ColorConstants.BLUE));
        table.addCell(new Cell().add(new Paragraph("QTY")
        ).setFontColor(ColorConstants.WHITE).setBackgroundColor(ColorConstants.BLUE));
        table.addCell(new Cell().add(new Paragraph("AMOUNT(INR)")
        ).setFontColor(ColorConstants.WHITE).setBackgroundColor(ColorConstants.BLUE));

        ProductItem[] productItems = (ProductItem[]) hashMapData.get("items");
        ProductItem productItem1 = productItems[0];
        ProductItem productItem2 = productItems[1];
        ProductItem productItem3 = productItems[2];
        ProductItem productItem4 = productItems[3];

        int subtotal = productItem1.totalAmount + productItem2.totalAmount
                +productItem3.totalAmount + productItem4.totalAmount;

        int discount = 0;

        int taxrate = 10;
        int tax = (subtotal/100)*taxrate;

        int invoiceTotal = subtotal+tax;




        //Row 7

        Text textInvoiceNumber = new Text("INVOICE NUMBER \n");
//
        textInvoiceNumber.setBold();
        textInvoiceNumber.setFontColor(new DeviceRgb(18,192,33));

        Paragraph paragraphInvoiceNumber = new Paragraph();
        paragraphInvoiceNumber.add(textInvoiceNumber);
        paragraphInvoiceNumber.add(hashMapData.get("invoiceNumber").toString());

        table.addCell(new Cell(2,1).add(paragraphInvoiceNumber).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(productItem1.name)
        ).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.cost))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));
        //.setBackgroundColor(new DeviceRgb(220,220,220))
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.qty))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.totalAmount))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));

        //Row 8
//        table.addCell(new Cell().add(new Paragraph()));
        table.addCell(new Cell().add(new Paragraph(productItem2.name))
                .setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.cost))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.qty))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.totalAmount))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));



        //Row 9

        Text textDateIssue = new Text("DATE OF ISSUE \n");
//
        textDateIssue.setBold();
        textDateIssue.setFontColor(new DeviceRgb(18,192,33));

        Paragraph paragraphDateIssue = new Paragraph();
        paragraphDateIssue.add(textDateIssue);
        paragraphDateIssue.add(hashMapData.get("issueDate").toString());


        table.addCell(new Cell(2,1).add(paragraphDateIssue).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(productItem3.name))
                .setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.cost))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.qty))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.totalAmount))
        ).setBackgroundColor(new DeviceRgb(220,220,220)));

        //Row 10
//        table.addCell(new Cell().add(new Paragraph()));
        table.addCell(new Cell().add(new Paragraph(productItem4.name))
                .setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.cost)))
                .setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.qty)))
                .setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.totalAmount)))
                .setBackgroundColor(new DeviceRgb(220,220,220)));

        //Row 11
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Row 12
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("SUBTOTAL")).setFontColor(new DeviceRgb(18,192,33)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(subtotal))));

        //Row 13
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("DISCOUNT")).setFontColor(new DeviceRgb(18,192,33)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(discount))));

        //Row 14
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("(TAX RATE)")).setFontColor(new DeviceRgb(18,192,33)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(taxrate))));

        //Row 15
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("TAX")).setFontColor(new DeviceRgb(18,192,33)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(tax))));

        //Row 16
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell(1,2).add(new Paragraph("==========").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
//        table.addCell(new Cell().add(new Paragraph()));

        //Row 17

        Text textInvoiceTotal = new Text("INVOICE TOTAL\n");
//
        textInvoiceTotal.setBold();
        textInvoiceTotal.setFontColor(new DeviceRgb(66,133,244)).setFontSize(16);

        Paragraph paragraphInvoiceTotal = new Paragraph();
        paragraphInvoiceTotal.add(textInvoiceTotal);
        paragraphInvoiceTotal.add(String.valueOf(invoiceTotal));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell(1,2).add(paragraphInvoiceTotal));
//        table.addCell(new Cell().add(new Paragraph()));

        //Row 18
        table.addCell(new Cell(1,2).add(new Paragraph("Terns\nE.g Please pay Invoice by "+hashMapData.get("dueDate"))).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
//        table.addCell(new Cell().add(new Paragraph()));





        return table;
    }


    private Bitmap generatePDFBitmap(File file) throws IOException {
        int WIDTH = 1440 ;
        int HEIGHT = 2392;
        ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer renderer = new PdfRenderer(fd);
        Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        PdfRenderer.Page page = renderer.openPage(0);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        return bitmap;


    }



}
