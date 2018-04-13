import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SYSTEM {
	static HSSFWorkbook workbook = new HSSFWorkbook();
	static XSSFSheet sheet;
	static FileOutputStream fileOut;
	static String filename="C:\\Users\\tanuj\\Documents\\test.xls" ;
	static	ArrayList<ArrayList<Integer>> combosingles= new ArrayList<ArrayList<Integer>>();
	public static void main(String[] args) throws IOException {

		ArrayList<ArrayList<Integer>> comb2= new ArrayList<ArrayList<Integer>>();
		//to store elements from 3 onwards
		ArrayList<ArrayList<ArrayList<Integer>>> mainlist=new ArrayList<ArrayList<ArrayList<Integer>>>();


		//this will create single element arraylist
		//change the length accordingly
		for(int i=0;i<5;i++)
		{
			ArrayList<Integer> temp=new ArrayList<Integer>();
			temp.add(i);
			combosingles.add(temp);
		}
		System.out.println(combosingles);
		//2 elememts list
		for(int i=0;i<5;i++)
		{

			for(int j=i+1;j<5;j++)
			{
				ArrayList<Integer> temp=new ArrayList<Integer>();
				for(Integer k:combosingles.get(i))
				{
					temp.add(k);
				}
				for(Integer k:combosingles.get(j))
				{
					temp.add(k);
				}
				comb2.add(temp);
			}

		}
		System.out.println(comb2);
		
		mainlist.add(comb2);
		for(int index=0;index<combosingles.size()-2;index++)
		{
			mainlist.add(CreateSets(mainlist.get(index),index+3));
		}
		mainlist.add(0, combosingles);
		//CreateNewSheet();
		ManipulateandCreateNewSheets(mainlist);
		System.out.println("Job completed");
	}
	public static ArrayList<ArrayList<Integer>> CreateSets(ArrayList<ArrayList<Integer>> comb, int numberOfElements)
	{
		ArrayList<ArrayList<Integer>> tempcomb=new ArrayList<ArrayList<Integer>>();
		//4 elements list
		for(int i=0;i<comb.size();i++)
		{
			ArrayList<Integer> temp=new ArrayList<Integer>();
			for(Integer k:comb.get(i))
			{
				temp.add(k);
			}
			for(int j=0;j<combosingles.size();j++)
			{

				for(Integer k:combosingles.get(j))
				{
					if(!temp.contains(k))
					{
						temp.add(k);
					}
				}
				//remove duplicates from temp
				Set<Integer> hs1 = new HashSet<>();
				hs1.addAll(temp);
				temp.clear();
				temp.addAll(hs1);
				if(temp.size()==numberOfElements)
				{
					tempcomb.add(temp);
					temp=new ArrayList<Integer>();
					for(Integer k:comb.get(i))
					{
						temp.add(k);
					}
				}
			}

		}
		//clean 3 element set
		Set<ArrayList<Integer>> hs = new HashSet<ArrayList<Integer>>();
		hs.addAll(tempcomb);
		tempcomb.clear();
		tempcomb.addAll(hs);
		System.out.println(tempcomb);
		return tempcomb;
	}


	public static void ManipulateandCreateNewSheets(ArrayList<ArrayList<ArrayList<Integer>>> mainlist) throws IOException
	{
		int count =0;
		for(int j=0;j<mainlist.size();j++)
		{
			
				for(int l=0;l<mainlist.get(j).size();l++)
				{
					ArrayList<Integer> temp=new ArrayList<Integer>();
					//temp will contain the columns that needs to be deleted
					for(int i=0;i<5;i++)
					{
						if(!mainlist.get(j).get(l).contains(i))
						{
							temp.add(i);
						}
					}
					FileInputStream fsIP= new FileInputStream(new File("C:\\Users\\tanuj\\Documents\\test.xls")); //Read the spreadsheet that needs to be updated

					HSSFWorkbook wb = new HSSFWorkbook(fsIP); //Access the workbook

					HSSFSheet worksheet = wb.getSheetAt(0); //Access the worksheet, so that we can update / modify it.

					Cell cell = null; // declare a Cell object
					for(Integer m:temp)
					{
						//this loop deletes the cells
						for(int i=0;i<worksheet.getLastRowNum();i++)
						{
							cell = worksheet.getRow(i).getCell(m); 

							// Access the second cell in second row to update the value
							try
							{
								cell.setCellValue("");  // Get current cell value value and overwrite the value
							}
							catch(Exception ex)
							{
								break;
							}


						}
					}//Close the InputStream
					fsIP.close(); 
					FileOutputStream output_file =new FileOutputStream(new File("C:\\Users\\tanuj\\Documents\\"+count+".xls"));  //Open FileOutputStream to write updates

					wb.write(output_file); //write changes

					output_file.close();  //close the stream    
					count++;
				}
			
		}
	}
}
