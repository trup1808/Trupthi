

import java.io.*;
import java.util.*;



public class Demo {
private SecondaryIndex[] sI = new SecondaryIndex[110010];
	
    private String Identifier,Name,Gender,specialization,NOServices;
	int reccount = 0;

	

public void getData(){
    		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the primary key: ");
		Identifier = scanner.next();
		System.out.println("enter the  name");
		Name = scanner.next();
		System.out.println("Enter the gender: ");
		Gender = scanner.next();
		System.out.println("Enter the specialization: ");
		specialization = scanner.next();
		System.out.println("Enter the noserivices: ");
		NOServices = scanner.next();
		
		
}
public void add(){
        String data=Identifier +","+ Name +","+  Gender +","+ specialization +","+ NOServices;

 try{			
			RandomAccessFile recordfile = new RandomAccessFile ("C:\\Users\\Admin\\Desktop\\ohh\\t.csv","rw");
			recordfile.seek(recordfile.length());
			long pos = recordfile.getFilePointer();
			recordfile.writeBytes(data+"\n");
			recordfile.close();
			
			RandomAccessFile indexfile = new RandomAccessFile ("secondary2.txt","rw");
			indexfile.seek(indexfile.length());
			indexfile.writeBytes(NOServices+","+pos+"\n");
			indexfile.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
		
 
}                     
    @SuppressWarnings("resource")
	public void secIndex(){

		String line
                        ,sec = null,pos = null;
		int count = 0;
		int sIIndex = 0;
		reccount=0;
		RandomAccessFile indexfile;
		 long starttime =System.nanoTime();
		try {
			indexfile = new RandomAccessFile("secondary2.txt","rw");

			try {
				
				while((line = indexfile.readLine())!= null){
                                     if(line.contains("*")) {
	                		continue;
	                	}
					count = 0;
                                                 
                                       
	          
					
					StringTokenizer st = new StringTokenizer(line,",");
					while (st.hasMoreTokens()){
					 count+=1;
					 if(count==1)
				    sec = st.nextToken();
					 pos = st.nextToken();
                                         
				    }
					sI[sIIndex] = new SecondaryIndex();
					sI[sIIndex].setRecPos(pos);
					sI[sIIndex].setsec(sec);
					reccount++;
					sIIndex++;
                                        if(sIIndex==110010)
                                        {
                                            break;
                                        }
                                }
			} catch (IOException e) {
				
				e.printStackTrace();
				return;
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return;
		} 
		
		System.out.println("total records" + reccount);
		long endtime =System.nanoTime();
        long totaltime=endtime-starttime;       
        System.out.println(totaltime/1000000+"msec");
         
		if (reccount==1) { return;}
		sortIndex();
	}
	
	
	public void sortIndex() {
		SecondaryIndex temp = new SecondaryIndex();
		
		for(int i=0; i<reccount; i++)
		    {	
				for(int j=i+1; j<reccount; j++) {
					if(sI[i].getsec().compareTo(sI[j].getsec())  > 0)
		            {
		                temp.setsec(sI[i].getsec()); 
				        temp.setRecPos(sI[i].getRecPos());
				
			        	sI[i].setsec(sI[j].getsec());
			        	sI[i].setRecPos(sI[j].getRecPos());
				
			        	sI[j].setsec(temp.getsec());
			        	sI[j].setRecPos(temp.getRecPos());
		            }
				}
					
			}	
		
	}
        @SuppressWarnings("resource")
		public void search(){
        	 System.out.println("Enter the NOServices to search: ");
             Scanner scanner = new Scanner(System.in);
             String NOServices = scanner.next();
             
             
             int oripos = binarySearch(sI, 0, reccount-1, NOServices);
             
             if (oripos == -1) {
                 System.out.println("Record not found in the record file");
                 return ;
             }
             
             int pos = oripos;
             
             do {
                 readFile(pos);
                 pos--;
                 if (pos < 0) { break;}
             }while(sI[pos].getsec().compareTo(NOServices)==0);
             
             pos = oripos + 1 ;
          // if (pos > reccount-1) { return;}
             
             while(sI[pos].getsec().compareTo(NOServices)==0 && pos > reccount-1) {
                 readFile(pos);
                 pos++;
                // if (pos > reccount-1) { break;}
             }
        }
 public void readFile(int pos) {
            
	 if (pos == -1) {
			System.out.println("Record not found in the record file");
			return ;}
            RandomAccessFile recordfile;
            try {
                recordfile = new RandomAccessFile ("C:\\Users\\Admin\\Desktop\\ohh\\t.csv","rw");
                try {
                    recordfile.seek(Long.parseLong(sI[pos].getRecPos()));
                    String record = recordfile.readLine();
                    StringTokenizer st = new StringTokenizer(record,",");
                    
                    int count = 0;
                       
                    while (st.hasMoreTokens()){
                             count += 1;
                               if(count==1){
                               String tmp_Identifier = st.nextToken();
                               System.out.println("prikey: "+tmp_Identifier);
                               this.Identifier = tmp_Identifier;
                               
                               String tmp_Name = st.nextToken();
                               System.out.println("NAME: "+tmp_Name);
                               this.Name = tmp_Name;
                              
                                String tmp_Gender = st.nextToken();
                               System.out.println("gender: "+tmp_Gender);
                               this.Gender = tmp_Gender;
                                
                                String tmp_specialization = st.nextToken();
                                System.out.println("specialization: "+tmp_specialization);
                                this.specialization = tmp_specialization;
                               
                                 String tmp_NOServices = st.nextToken();
                                 System.out.println("NOServices: "+tmp_NOServices);
                                 this.NOServices = tmp_NOServices;
                               
                                
                                   
                                 
                                   
                               }
                               else
                                   break;
                       }
                    
                    recordfile.close();
                } 
                    catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                
                }
                                        
                catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
 }

        int binarySearch(SecondaryIndex s[], int l, int r, String NOServices) {
    	
    	int mid;
    	while (l<=r) {
            
    		mid = (l+r)/2;
    		if(s[mid].getsec().compareTo(NOServices)==0)
    		{
    			return mid;
    			}
    		if(s[mid].getsec().compareTo(NOServices)<0)
    		{
    			l = mid + 1;
    		}
    			if(s[mid].getsec().compareTo(NOServices)>0) 
    		{
    				r = mid - 1;
    				}
    	}
    	return -1;
    }

  public  void indexing() 
  {
	  long starttime =System.nanoTime();
  
  
 
         try{
        RandomAccessFile hey=new RandomAccessFile("C:\\Users\\Admin\\Desktop\\ohh\\t.csv","rw");
  
    			

        RandomAccessFile indexfile=new RandomAccessFile("secondary2.txt","rw");
        String line;
 long       pos=hey.getFilePointer();
        while((line = hey.readLine())!=null)
        {
            if(line.contains("*")) {
	                		continue;
	                	}

            String[] columns=line.split(",");
                                 

                        

         
            indexfile.writeBytes(columns[3]+","+pos+"\n");
            pos=hey.getFilePointer();
        } indexfile.close();
        hey.close();
        long endtime =System.nanoTime();
        long totaltime=endtime-starttime;       
        System.out.println(totaltime/1000000+"msec");
         
       
         }
    
    catch(IOException e)
    {
        System.out.println(e);
    }
  }

 @SuppressWarnings("resource")
public   void delete() throws IOException {
	 indexing();
     
     System.out.println("Enter the NOServices to delete: ");
     Scanner scanner = new Scanner(System.in);
     String NOServices = scanner.next();
     String ans = "n";
     int pos;
     
     int oripos = binarySearch(sI, 0, reccount-1, NOServices);
     
     if (oripos == -1) {
         System.out.println("Record not found in the record file");
         return ;
     }
     
     pos = oripos;
     
     do {
         readFile(pos);
         
         System.out.println("Do You Want To delete This Record ?(y/n) ");
         ans = scanner.next();
         
         if (ans.compareTo("y")==0) {
             markDelete(NOServices, pos);
         }
         pos--;
         if (pos < 0) { break;}
     }while(sI[pos].getsec().compareTo(NOServices)==0);
         
     
     pos = oripos + 1 ;
     
   //  if (pos > reccount-1) { return;}
     while(sI[pos].getsec().compareTo(NOServices)==0 && pos > reccount-1){
         readFile(pos);
         
         System.out.println("Do You Want To delete This Record ?(y/n) ");
         ans = scanner.next();
         
         if (ans.compareTo("y")==0) {
             markDelete(NOServices,pos);
             break;
         }
         pos++;
         if (pos > reccount-1) { break;}
     }
}
 @SuppressWarnings("resource")
public void markDelete(String NOServices,int pos) {
     try {
         RandomAccessFile recordfilee = new RandomAccessFile ("C:\\Users\\Admin\\Desktop\\ohh\\t.csv","rw");
         RandomAccessFile indexfilee = new RandomAccessFile ("secondary2.txt","rw");
     
             recordfilee.seek(Long.parseLong(sI[pos].getRecPos()));
             recordfilee.writeBytes("*");
             System.out.println("Done");
             recordfilee.close();
             String line;
             String indexName;
             long indexPos = 0;
             long delPosi;
             //delPosi = indexfilee.getFilePointer();
             while((line = indexfilee.readLine())!=null) {
                 if(line.contains("*"))
                     continue;
                 StringTokenizer st = new StringTokenizer(line,",");
               delPosi = indexfilee.getFilePointer();
                delPosi = delPosi - (line.length()+2);
                 
                // System.out.println("Delposi: " + delPosi);
                 
                 while(st.hasMoreTokens()) {
                     indexName=st.nextToken();
                     indexPos= Long.parseLong(st.nextToken());
                  //   System.out.println("indexPos: " + indexPos);
                    // System.out.println("getrecpos: " + Long.parseLong(sI[pos].getRecPos()));
                     if(indexName.equals(NOServices) && indexPos == Long.parseLong(sI[pos].getRecPos()) ) {
                         indexfilee.seek(delPosi);
                         indexfilee.writeBytes("*");
                         indexfilee.close();
                         System.out.println("Deleted");
                         indexing();
                         return;
                     }
                 }
             }
             }
         
         catch (Exception e) {
             e.printStackTrace();
         }
 }

}

                                                 
                                       
	          
					
					