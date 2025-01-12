package com.example.m08;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.m08.Penyewaan.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class PDFService {
    
    public ByteArrayInputStream generateLaporanPDF(List<Penyewaan> penyewaans) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Laporan Penyewaan Film", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));
            
            Font dateFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Paragraph date = new Paragraph("Tanggal: " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()), dateFont);
            document.add(date);
            document.add(new Paragraph("\n"));
            
            PdfPTable table = new PdfPTable(7); 
            table.setWidthPercentage(100);
            
            Stream.of("No.", "Judul Film", "Genre", "Tanggal Pinjam", "Tanggal Kembali", "Status", "Harga")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setPadding(5);
                    table.addCell(header);
                });
            
            int number = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            
            for (Penyewaan penyewaan : penyewaans) {
                table.addCell(String.valueOf(number++));
                table.addCell(penyewaan.getNama_Film());
                table.addCell(penyewaan.getNama_Genre());
                table.addCell(sdf.format(penyewaan.getTanggal()));
                table.addCell(penyewaan.getTanggal_Kembali() != null ? 
                    sdf.format(penyewaan.getTanggal_Kembali()) : "-");
                table.addCell(penyewaan.getStatus());
                double harga = penyewaan.getHarga();
                table.addCell(String.valueOf(harga));
            }
            
            document.add(table);
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total Penyewaan: " + penyewaans.size()));
            long activeCount = penyewaans.stream()
                .filter(p -> "ACTIVE".equals(p.getStatus()))
                .count();
            document.add(new Paragraph("Penyewaan Aktif: " + activeCount));
            document.add(new Paragraph("Penyewaan Selesai: " + (penyewaans.size() - activeCount)));
            double totalPendapatan = penyewaans.stream()
                .mapToDouble(Penyewaan::getHarga)
                .sum(); 
            document.add(new Paragraph("Total Pendapatan: " + totalPendapatan));
            
            document.close();
            
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
        return new ByteArrayInputStream(out.toByteArray());
    }
}