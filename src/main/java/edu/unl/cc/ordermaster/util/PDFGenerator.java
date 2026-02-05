package edu.unl.cc.ordermaster.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import edu.unl.cc.ordermaster.domain.ComprobanteVenta;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PDFGenerator {
    private String ruta;
    //relacion
    private ComprobanteVenta comprobante;
    //no
    private Document document;
    private FileOutputStream fos;

    public PDFGenerator() {
    }

    //inicializaParrafos
    Font titulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14);
    Font parrafo = FontFactory.getFont(FontFactory.HELVETICA, 12);

    private void rutaPredefinida(){
        this.ruta = "D:\\Usuarios\\Franz\\Documents\\Pruebas\\";
    }

    private void Creardocumento() throws DocumentException, FileNotFoundException {
        document = new Document(PageSize.A4,35,35,50,50);
        fos = new FileOutputStream(getRuta());
        PdfWriter.getInstance(document,fos);
    }
    private void abrirDocumento(){
        document.open();
    }

    public void generar(String texto, ComprobanteVenta comprobante) throws DocumentException, FileNotFoundException {
        Creardocumento();
        abrirDocumento();
        Paragraph para = new Paragraph();
        para.add(new Paragraph(texto, parrafo));
        document.add(para);
        cerrarDocumento();
    }
    private void cerrarDocumento(){
        document.close();
        System.out.println("pdf generado");
    }

    public String getRuta() {
        return ruta+"\\Comprovante"+comprobante.getPedido().getCliente().getNombreCompleto()+".pdf";
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
