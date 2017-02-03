package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
		try{
			CSVReader reader = new CSVReader(new StringReader(csvString));
			List<String[]> full = reader.readAll();
			Iterator<String[]> iterator = full.iterator();
			JSONObject jObject = new JSONObject();
			JSONArray rowHeaders = new JSONArray(), mainData = new JSONArray(), colHeaders = new JSONArray();
			for (String string: iterator.next()){
				colHeaders.add(string);
			}
			while (iterator.hasNext()){
				JSONArray rowData = new JSONArray();
				String[] rows = iterator.next();
				rowHeaders.add(rows[0]);
				for (int i = 1; i < rows.length; i++){
					rowData.add(rows[i]);
				}
				mainData.add(rowData);
			}
			
			StringBuilder s = new StringBuilder();
			s.append("{\n    \"colHeaders\":" + colHeaders.toString());
			s.append(",\n    \"rowHeaders\":" + rowHeaders.toString() + ",\n");
			String[] dataRows = mainData.toString().split("],"); 
			s.append("    \"data\":");							
			for (String string: dataRows){
				string = string.replace("\"","");
				s.append(string + "],\n            ");
			}
			s = s.replace(s.length()-16,s.length(),"");
			s.append("\n    ]\n}");
			return s.toString();
		}
		catch(IOException e){return "";}
    }
    
    public static String jsonToCsv(String jsonString) { 
		try{
			JSONParser parser = new JSONParser();
			JSONObject jobject = (JSONObject) parser.parse(jsonString);
			JSONArray colHeaders = (JSONArray) jobject.get("colHeaders");
			JSONArray rowHeaders = (JSONArray) jobject.get("rowHeaders");
			JSONArray data = (JSONArray) jobject.get("data");
			
			Object[] colArray = colHeaders.toArray(), rowArray = rowHeaders.toArray(), dataArray = data.toArray();
			String[] colStringArray = new String[colArray.length];
			String[] rowStringArray = new String[rowArray.length];
			String[] dataStringArray = new String[dataArray.length];
			
			for (int i = 0; i < colArray.length; i++){
				colStringArray[i] = colArray[i] + "";
			}
			for (int i = 0; i < rowArray.length; i++){
				rowStringArray[i] = rowArray[i] + "";
				dataStringArray[i] = dataArray[i] + "";
			}
			
			StringWriter writer = new StringWriter();
			CSVWriter csvWriter = new CSVWriter(writer,',','"',"\n");
			csvWriter.writeNext(colStringArray);
			for (int i = 0; i < dataStringArray.length; i++){
				String[] split = dataStringArray[i].split(",");
				String[] row = new String[split.length + 1];
				row[0] = rowStringArray[i];
				for (int j = 0; j < split.length; j++){
					split[j] = split[j].replace("[","");
					split[j] = split[j].replace("]","");
					row[j+1] = split[j];
				}
				csvWriter.writeNext(row);
			}
			return writer.toString();
		}
		catch(ParseException pe){return "";}
    }
	
}