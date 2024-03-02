package com.nilknow.yifanerp2.util;

import com.nilknow.yifanerp2.entity.Category;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.excel.ProductExcelTemplate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelUtil {
    public static void createProductSheet(XSSFSheet sheet, List<ProductExcelTemplate> rows) {
        XSSFRow header = sheet.createRow(0);
        XSSFCell c1 = header.createCell(0);
        c1.setCellValue("名称");
        XSSFCell c2 = header.createCell(1);
        c2.setCellValue("数量");
        for (int i = 0; i < rows.size(); i++) {
            ProductExcelTemplate product = rows.get(i);
            XSSFRow row = sheet.createRow(i + 1);
            XSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(product.getName());
            XSSFCell dataCell2 = row.createCell(1);
            dataCell2.setCellValue(product.getCount());
        }
        // 512 is the unit of chinese character width
        sheet.setColumnWidth(0,8*512); // 8 chinese characters width
        sheet.setColumnWidth(1,3*512);
    }

    public static void createMaterialSheet(XSSFSheet sheet, List<Material> rows){
        XSSFRow header = sheet.createRow(0);
        XSSFCell c1 = header.createCell(0);
        c1.setCellValue("名称");
        XSSFCell c2 = header.createCell(1);
        c2.setCellValue("数量");
        XSSFCell c3 = header.createCell(2);
        c3.setCellValue("库存预警数量");
        for (int i = 0; i < rows.size(); i++) {
            Material material = rows.get(i);
            XSSFRow row = sheet.createRow(i + 1);
            XSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(material.getName());
            XSSFCell dataCell2 = row.createCell(1);
            dataCell2.setCellValue(material.getCount());
            XSSFCell dataCell3 = row.createCell(1);
            dataCell3.setCellValue(material.getInventoryCountAlert());
        }
        sheet.setColumnWidth(0,8*512); // 8 chinese characters width
        sheet.setColumnWidth(1,3*512);
        sheet.setColumnWidth(2,7*512);
    }

    public static void exportProducts(OutputStream os, List<Product> products) {
        Map<String, List<Product>> productMap = products.stream().collect(Collectors.groupingBy(x -> x.getCategory().getName()));
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            for (Map.Entry<String, List<Product>> entry : productMap.entrySet()) {
                String category = entry.getKey();
                XSSFSheet sheet = wb.createSheet(category);
                XSSFRow header = sheet.createRow(0);
                XSSFCell c1 = header.createCell(0);
                c1.setCellValue("名称");
                XSSFCell c2 = header.createCell(1);
                c2.setCellValue("数量");
                List<Product> subProducts = entry.getValue();
                for (int i = 0; i < subProducts.size(); i++) {
                    Product product = subProducts.get(i);
                    XSSFRow row = sheet.createRow(i + 1);
                    XSSFCell cell1 = row.createCell(0);
                    cell1.setCellValue(product.getName());
                    XSSFCell dataCell2 = row.createCell(1);
                    dataCell2.setCellValue(product.getCount());
                }
                // 512 is the unit of chinese character width
                sheet.setColumnWidth(0,8*512); // 8 chinese characters width
                sheet.setColumnWidth(1,3*512);
            }

            wb.write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportMaterials(OutputStream os, List<Material> materials) {
        Map<String, List<Material>> materialMap = materials.stream().collect(Collectors.groupingBy(Material::getCategory));
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            for (Map.Entry<String, List<Material>> entry : materialMap.entrySet()) {
                String category = entry.getKey();
                XSSFSheet sheet = wb.createSheet(category);
                XSSFRow header = sheet.createRow(0);
                XSSFCell c1 = header.createCell(0);
                c1.setCellValue("名称");
                XSSFCell c2 = header.createCell(1);
                c2.setCellValue("数量");
                XSSFCell c3 = header.createCell(2);
                c3.setCellValue("库存预警数量");
                List<Material> subMaterial = entry.getValue();
                for (int i = 0; i < subMaterial.size(); i++) {
                    Material material = subMaterial.get(i);
                    XSSFRow row = sheet.createRow(i + 1);
                    XSSFCell cell1 = row.createCell(0);
                    cell1.setCellValue(material.getName());
                    XSSFCell dataCell2 = row.createCell(1);
                    dataCell2.setCellValue(material.getCount());
                    XSSFCell dataCell3 = row.createCell(2);
                    dataCell3.setCellValue(material.getInventoryCountAlert());
                }
                sheet.setColumnWidth(0,8*512); // 8 chinese characters width
                sheet.setColumnWidth(1,3*512);
                sheet.setColumnWidth(2,7*512);
            }

            wb.write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Product> getProducts(InputStream is, Set<Category> categoryCache) throws Exception {
        List<Product> products = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            // validate header names
            if (rowIterator.hasNext()) {
                Row header = rowIterator.next();

                Cell nameHeader = header.getCell(0);
                Cell countHeader = header.getCell(1);
                if (!"名称".equals(nameHeader.getStringCellValue())) {
                    throw new Exception("first header name must be 名称");
                }
                if (!"数量".equals(countHeader.getStringCellValue())) {
                    throw new Exception("second header name must be 数量");
                }
            }

            // get all products values
            String categoryName = sheet.getSheetName();
            Optional<Category> category = categoryCache.stream().filter(x -> x.getName().equals(categoryName)).findAny();
            if (category.isEmpty()) {
                //todo category not exist
                return new ArrayList<>();
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Cell nameCell = row.getCell(0);
                Cell countCell = row.getCell(1);

                String name = nameCell.getStringCellValue();
                long count = (long) countCell.getNumericCellValue();

                Product product = new Product();
                product.setName(name);
                product.setCount(count);
                product.setCategory(category.get());
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during the operation
        }

        return products;
    }

    public static List<Material> getMaterials(InputStream is) throws Exception {
        List<Material> materials = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            int sheetCounts = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetCounts; i++) {
                Sheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();

                // validate header names
                if (rowIterator.hasNext()) {
                    Row header = rowIterator.next();

                    Cell nameHeader = header.getCell(0);
                    Cell countHeader = header.getCell(1);
                    if (!"名称".equals(nameHeader.getStringCellValue())) {
                        throw new Exception("first header name must be 名称");
                    }
                    if (!"数量".equals(countHeader.getStringCellValue())) {
                        throw new Exception("second header name must be 数量");
                    }
                }

                // get all materials values
                String category = sheet.getSheetName();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    Cell nameCell = row.getCell(0);
                    Cell countCell = row.getCell(1);

                    String name = nameCell.getStringCellValue();
                    long count = (long) countCell.getNumericCellValue();

                    Material material = new Material();
                    material.setName(name);
                    material.setCount(count);
                    material.setCategory(category);
                    materials.add(material);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during the operation
        }

        return materials;
    }
}
