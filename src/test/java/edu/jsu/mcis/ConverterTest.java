package edu.jsu.mcis;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConverterTest {
    private String csvString;
    private String jsonString;

    @Before
    public void setUp() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        StringBuffer csvContents = new StringBuffer();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("grades.csv")))) {
            String line;
            while((line = reader.readLine()) != null) {
                csvContents.append(line + '\n');
            }
        }
        catch(IOException e) { e.printStackTrace(); }
        csvString = csvContents.toString();
        
        StringBuffer jsonContents = new StringBuffer();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("grades.json")))) {
            String line;
            while((line = reader.readLine()) != null) {
                jsonContents.append(line + '\n');
            }
        }
        catch(IOException e) { e.printStackTrace(); }
        jsonString = jsonContents.toString();
    }
    
    @Test
    public void testConvertCSVtoJSON() {
		assertEquals(jsonString, Converter.csvToJson(csvString) + "\n");
    }

    @Test
    public void testConvertJSONtoCSV() {
       assertEquals(csvString, Converter.jsonToCsv(jsonString));
    }
	
	@Test
	public void testConvertJSONtoCSVtoJSON(){
		String csvTest = Converter.jsonToCsv(jsonString);
		String jsonTest = Converter.csvToJson(csvTest) + "\n";
		assertEquals(jsonString, jsonTest);
	}
	
	@Test
	public void testConvertCSVtoJSONtoCSV(){
		String jsonTest = Converter.csvToJson(csvString) + "\n";
		String csvTest = Converter.jsonToCsv(jsonTest);
		assertEquals(csvString, csvTest);
	}
}







