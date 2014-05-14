import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Topo {
	private String path;
	private double[][] points;
	private boolean[][]  correc1;
	private int fx;
	private int fy;
	
	public Topo(String pathFile,int fx, int fy){
		this.path = pathFile;
		this.points =  new double[fy+1][fx+1];
		this.correc1 = new boolean[fy+1][fx+1];
		this.fx = fx;
		this.fy = fy;
	}
	
	public void concatH(String pathFile2, int fx2, int fy2){
		double[][] points2 = new double[fy2+1][fx2+1];
		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(pathFile2); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			br.readLine();
			String test = "";
			while (test != null){
				br.readLine();
				br.readLine();
				for(int i = 0 ; i < 3; i++ ){
					String l = br.readLine();
					String [] lcut= l.split(" ");
					int x = Integer.parseInt(lcut[8]);
					int y = Integer.parseInt(lcut[9]);
					double z = Double.parseDouble(lcut[10]);
					if(points2[y][x] == 0){
						points2[y][x] = z;
					}
				}
				br.readLine();
				test = br.readLine();
			}
			br.close(); 
		}catch (Exception e){
			e.printStackTrace();
		}
		
		double[][] res = new double[fy+fy2+2][fx+1];
		for(int y = 0 ; y < (fy+1); y++){
			for(int x = 0 ; x < (fx+1) ;  x++){
				res[y][x] = this.points[y][x];
			}
		}
		for(int y = 0 ; y < (fy+1); y++){
			for(int x = 5 ; x < (fx2+1)+5 ;  x++){
				res[y+fy+1][x] = points2[y][x-5];
			}
		}
		
		this.points = res;
		this.fy= 2*fy;
	}
	
	public void concatD(String pathFile2, int fx2, int fy2){
		
		double[][] points2 = new double[fy2+1][fx2+1];
		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(pathFile2); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			br.readLine();
			String test = "";
			while (test != null){
				br.readLine();
				br.readLine();
				for(int i = 0 ; i < 3; i++ ){
					String l = br.readLine();
					String [] lcut= l.split(" ");
					
					int x = Integer.parseInt(lcut[8]);
					int y = Integer.parseInt(lcut[9]);
					double z = Double.parseDouble(lcut[10]);
					if(points2[y][x] == 0){
						points2[y][x] = z;
					}
				}
				br.readLine();
				test = br.readLine();
			}
			br.close(); 
		}catch (Exception e){
			e.printStackTrace();
		}
		
		double[][] res = new double[fy+1][fx+fx2+2];
		for(int y = 0 ; y < (fy+1); y++){
			for(int x = 0 ; x < (fx+1) ;  x++){
				res[y][x] = this.points[y][x];
			}
		}
		for(int y = 0 ; y < (fy+1); y++){
			for(int x = 0 ; x < (fx2+1) ;  x++){
				res[y][fx+1+x] = points2[y][x];
			}
		}
		
		this.points = res;
		this.fx = fx+fx2;
	}
	
	public void readFile(){
		
		//lecture du fichier texte	
				try{
					InputStream ips=new FileInputStream(this.path); 
					InputStreamReader ipsr=new InputStreamReader(ips);
					BufferedReader br=new BufferedReader(ipsr);
					br.readLine();
					String test = "";
					while (test != null){
						br.readLine();
						br.readLine();
						for(int i = 0 ; i < 3; i++ ){
							String l = br.readLine();
							String [] lcut= l.split(" ");
							/*for(int i1 = 0 ; i1< lcut.length ; i1++){
								System.out.println(i1 + " " +lcut[i1]);
							}*/
							int x = Integer.parseInt(lcut[8]);
							int y = Integer.parseInt(lcut[9]);
							double z = Double.parseDouble(lcut[10]);
							if(this.points[y][x] == 0){
								this.points[y][x] = z;
							}
						}
						br.readLine();
						test = br.readLine();
					}
					br.close(); 
				}catch (Exception e){
					e.printStackTrace();
				}
	}
	
	public void correct(){
		for(int y = 0 ; y < fy; y++){
			for(int x = 0 ; x < (fx+1) ;  x++){
				if(x == 0 && y == 0){
					System.out.println(this.points[y][x]);
				}
				if(this.points[y][x] == 0){
					try{
					int dy = y+1;
					int d = 2;
					while(this.points[dy][x] == 0){
						dy++;
						d++;
					}
					if(this.points[y-1][x] == 200){
						this.points[y][x] = 200;
					}
					else
						this.points[y][x] = this.points[y-1][x] + (this.points[dy][x] - this.points[y-1][x])/d;
					}catch(Exception e){
						e.printStackTrace();
						this.points[y][x]= 200.00;
					}
					
					this.correc1[y][x]= true;
				}
		}
		}
	}

	public void correct2(){
	for(int x = 0 ; x < fx ;  x++){
		for(int y = 0 ; y < (fy+1); y++){
				if(this.correc1[y][x] == true){
					try{
					int dx = x+1;
					int d = 2;
					while(this.correc1[y][dx] == true){
						dx++;
						d++;
					}
					if(x == 1 && y == 185){
						System.out.println(this.points[y][x]);
					}
					double temp = this.points[y][x-1] + (this.points[y][dx] - this.points[y][x-1])/d;
					if(this.points[y][x] != 200.00){
						this.points[y][x] = (this.points[y][x]+temp)/2;
					}else if(this.points[y][x] == 200) {
						this.points[y][x] = temp;
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void writeNewFile(){
		try {
			FileWriter fw = new FileWriter("out"+this.path);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw);
			fichierSortie.println ("solid "+"out"+this.path);
			for(int x = 0 ; x < fx ;  x++){
				for(int y = 0 ; y < fy; y++){
					
					// Deux triangles pour chaque y,x//
					//Premier triangle//
					fichierSortie.println("facet normal 0  0  0");
					fichierSortie.println("outer loop");
					
					fichierSortie.println("      vertex  "+x+" "+y+" "+this.points[y][x]);
					fichierSortie.println("      vertex  "+x+" "+(y+1)+" "+this.points[y+1][x]);
					fichierSortie.println("      vertex  "+(x+1)+" "+(y+1)+" "+this.points[y+1][x+1]);				
					
					fichierSortie.println("endloop");
					fichierSortie.println("endfacet");
					
					// Deuxieme triangle //
					fichierSortie.println("facet normal 0  0  0");
					fichierSortie.println("outer loop");
					
					fichierSortie.println("      vertex  "+x+" "+y+" "+this.points[y][x]);
					fichierSortie.println("      vertex  "+(x+1)+" "+(y+1)+" "+this.points[y+1][x+1]);
					fichierSortie.println("      vertex  "+(x+1)+" "+y+" "+this.points[y][x+1]);	
					
					fichierSortie.println("endloop");
					fichierSortie.println("endfacet");					
					///////////////////////
				}
			}
			
			fichierSortie.println ("endsolid "+"out"+this.path);
			fichierSortie.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}	
	}
	
	public void coupe(int x1,int x2, int y1, int y2){
		try {
			FileWriter fw = new FileWriter("out"+this.path);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw);
			fichierSortie.println ("solid "+"out"+this.path);
			for(int x = x1 ; x < x2 ;  x++){
				for(int y = y1 ; y < y2; y++){
					
					// Deux triangles pour chaque y,x//
					//Premier triangle//
					fichierSortie.println("facet normal 0  0  0");
					fichierSortie.println("outer loop");
					
					fichierSortie.println("      vertex  "+(x-x1)+" "+(y-y1)+" "+this.points[y][x]);
					fichierSortie.println("      vertex  "+(x-x1)+" "+(y+1-y1)+" "+this.points[y+1][x]);
					fichierSortie.println("      vertex  "+(x+1-x1)+" "+(y+1-y1)+" "+this.points[y+1][x+1]);				
					
					fichierSortie.println("endloop");
					fichierSortie.println("endfacet");
					
					// Deuxieme triangle //
					fichierSortie.println("facet normal 0  0  0");
					fichierSortie.println("outer loop");
					
					fichierSortie.println("      vertex  "+(x-x1)+" "+(y-y1)+" "+this.points[y][x]);
					fichierSortie.println("      vertex  "+(x+1-x1)+" "+(y+1-y1)+" "+this.points[y+1][x+1]);
					fichierSortie.println("      vertex  "+(x+1-x1)+" "+(y-y1)+" "+this.points[y][x+1]);	
					
					fichierSortie.println("endloop");
					fichierSortie.println("endfacet");					
					///////////////////////
				}
			}
			
			fichierSortie.println ("endsolid "+"out"+this.path);
			fichierSortie.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}	
	}
}
