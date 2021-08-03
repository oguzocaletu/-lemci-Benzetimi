import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Benzetim {
	static int bellekAdresiBoyutu;
	static int [] YazmacObegi= new int[32];
	static String[] BuyrukBelleði=new String[(int) Math.pow(2, 20)];
	static int [] VeriBelleði= new int[(int) Math.pow(2, 20)];//2^20 different address can be initialized
	private static  String programSayaci="";// program Sayaci baslangic degeri 0 en altta initialize ediliyor
	public static void main(String args[])  {
		for(int i=0;i<32;i++) {// Baslangicta Yazmaclar 0
			YazmacObegi[i]=0;
		}
		File file1=new File(args[0]);
		File file2=new File(args[1]);

		Scanner in;
		Scanner in2;
		Scanner in3;
		int frekans;
		int frekans2 = 0;
		int[] StableInformation=new int[6];//0-Frekans 1-R 2-I 3-B 4-S 5-J
		int[] StableInformation2=new int[6];
		try {
			in2 = new Scanner(file2);
			int index=0;
			while(in2.hasNextLine()) {//config bilgilerini alir
				String info=in2.nextLine();
				StableInformation[index++]=Integer.parseInt(info.substring(info.indexOf(" ")+1));
			}
			if(args.length==3) {//yeni güncelleme ile eðer 2 tane config girilirse
				File file3=new File(args[2]);
				in3 = new Scanner(file3);
				index=0;
				while(in3.hasNextLine()) {//config bilgilerini alir (2.config dosya)
					String info=in3.nextLine();
					StableInformation2[index++]=Integer.parseInt(info.substring(info.indexOf(" ")+1));
				}
				frekans2=StableInformation2[0];
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		frekans=StableInformation[0];
		String buyrukV;
		int R=0;
		int I=0;
		int B=0;
		int S=0;
		int J=0;
		int buyrukSayisi=0;
		

		int bellektekiSonAdres=0;
	

		//BUYRUK BELLEGÝNÝ OLUSTUR
		try {
			in = new Scanner(file1);
			
			while(in.hasNextLine()) {
				
				buyrukV=in.nextLine();
				
				if(buyrukV.length()>0) {
					int buyrukAdresi=Integer.parseInt(buyrukV.substring(buyrukV.indexOf("x")+1,buyrukV.indexOf(" ")),16);
					//buyruk adresini al ve hexden onluk tabana cevir
				
					BuyrukBelleði[buyrukAdresi]=buyrukV;//dogru alana yaz
					bellektekiSonAdres=buyrukAdresi;
				
					bellekAdresiBoyutu=buyrukV.indexOf(" ")-buyrukV.indexOf("x")-1;
					
	
				}
				
			}
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		PCFirstÝnitilaize();
		
		do {

			// ****************** GETÝRME AÞAMASI **************************
			String buyruk=BuyrukBelleði[Integer.parseInt(programSayaci,16)];
	
			if(buyruk!=null&&buyruk.length()>0) {
			//	System.out.println("PC:"+Integer.parseInt(programSayaci,16));
				if(buyruk.length()>0&&buyruk.substring(buyruk.indexOf("x")+1,buyruk.indexOf(" ")).equals(programSayaci)){
					//program counterýn bulunduðu adreste bir buyruk var mý kontrol et
				}
				else if(buyruk.length()>0){
					while(!buyruk.substring(buyruk.indexOf("x")+1,buyruk.indexOf(" ")).equals(programSayaci)) {
						IncProgramCounter();//program counterin bulundugu adreste bir buyruk yoksa program counteri arttir
					}
				}

				if(args.length!=3&&buyruk.substring(buyruk.indexOf(" ")+1).equals("SON")) {
					int toplamCevrim=(R*StableInformation[1]+I*StableInformation[2]+B*StableInformation[3]+S*StableInformation[4]+J*StableInformation[5]);
					System.out.println("Toplam Cevrim Sayisi: "+toplamCevrim);
					System.out.println("Yürütülen Buyruk Sayisi: "+buyrukSayisi);
					System.out.println("Saniye: "+((toplamCevrim*(1.0/frekans))/1000000));
					System.out.println("************Program bitti*************");
					 R=0;
					 I=0;
					 B=0;
					 S=0;
					 J=0;
					 buyrukSayisi=0;
					 break;
				}
				else if(args.length==3&&buyruk.substring(buyruk.indexOf(" ")+1).equals("SON"))
				{
					int toplamCevrim=(R*StableInformation[1]+I*StableInformation[2]+B*StableInformation[3]+S*StableInformation[4]+J*StableInformation[5]);
					int toplamCevrim2=(R*StableInformation2[1]+I*StableInformation2[2]+B*StableInformation2[3]+S*StableInformation2[4]+J*StableInformation2[5]);
					double islemci1=toplamCevrim*(1.0/frekans);
					double islemci2=toplamCevrim2*(1.0/frekans2);
					if(islemci1>islemci2) {
						System.out.printf("Islemci2’nin basarimi Islemci1’in basarimindan %.2f kat daha yuksek.",(islemci1/islemci2));
					}
					else if(islemci2>islemci1){
						System.out.printf("Islemci1’in basarimi Islemci2’nin basarimindan %.2f kat daha yuksek.",(islemci2/islemci1) );
					}
					else {
						System.out.println("Islemci1’in basarimi ve Islemci2’nin basarimi aynýdýr.");
					}
					break;
				}
				
					buyrukSayisi++;
				
				//******************* COZ ASAMASI ************************
				
				/*
				 * R|add-sub-xor-srl-sra
				 * I|addi-subi-xori-srai-slti-lw-lb-jalr
				 * B|beq-bge-blt
				 * S|sw-sb
				 * J|jal
				 */
				
				String buyrukLeft=buyruk.substring(buyruk.indexOf(" ")+1);
				
				String currentBuyruk=buyrukLeft.substring(0,buyrukLeft.indexOf(" "));
				buyrukLeft=buyrukLeft.substring(buyrukLeft.indexOf(" ")+1);
		
				int tempYazmac=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
		
				buyrukLeft=buyrukLeft.substring(buyrukLeft.indexOf(" ")+1);
				
	
				int kaynakYazmac1=0;
				int kaynakYazmac2=0;
				int hedefYazmac=0;
				int immediateValue=0;
				String currentType="";
	
				switch(currentBuyruk) {
					//R TYPE
					case "add":
						R++;
						currentType="R";
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+2));
						break;
					case "sub":
						currentType="R";
						R++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+2));
						break;
					case "xor":
						currentType="R";
						R++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+2));
						break;
					case "srl":
						currentType="R";
						R++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+2));
						break;
					case "sra":
						currentType="R";
						R++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+2));
						break;

						//I TYPE
					case "addi":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "subi":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "xori":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "srai":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "slti":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "lw":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "lb":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "jalr":
						currentType="I";
						I++;
						hedefYazmac=tempYazmac;
						kaynakYazmac1=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
						
						//B TYPE
					case "beq":
						currentType="B";
						B++;
						kaynakYazmac1=tempYazmac;
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "bge":
						currentType="B";
						B++;
						kaynakYazmac1=tempYazmac;
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "blt":
						currentType="B";
						B++;
						kaynakYazmac1=tempYazmac;
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
						//S TYPE
					case "sw":
						currentType="S";
						S++;
						kaynakYazmac1=tempYazmac;
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
					case "sb":
						currentType="S";
						S++;
						kaynakYazmac1=tempYazmac;
						kaynakYazmac2=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf("x")+1,buyrukLeft.indexOf(" ")));
						immediateValue=Integer.parseInt(buyrukLeft.substring(buyrukLeft.indexOf(" ")+1),16);
						break;
						//J TYPE
					case "jal":
						currentType="J";
						J++;
						hedefYazmac=tempYazmac;
						immediateValue=Integer.parseInt(buyrukLeft,16);
	
						break;

				}
				
				//******************************** YURUTME ASAMASI *************************************
				
				int yazmacDegeri1=0;
				int yazmacDegeri2=0;
				int anlikDeger=0;

				switch(currentType) {
				
				case "R"://R tipi buyruklar yürütme
					 yazmacDegeri1=YazmacObegi[kaynakYazmac1];
					 yazmacDegeri2=YazmacObegi[kaynakYazmac2];
					// YazmacObegi[hedefYazmac];
					switch(currentBuyruk) {
					
						case"add":
							YazmacObegi[hedefYazmac]=yazmacDegeri1+yazmacDegeri2;
							break;
						case"sub":
							YazmacObegi[hedefYazmac]=yazmacDegeri1+yazmacDegeri2;
							break;
						case"xor":
							String a =Integer.toBinaryString(yazmacDegeri1);
							String b =Integer.toBinaryString(yazmacDegeri2);
							String sonuc="";
							for(int i=0;i<a.length()||i<b.length();i++) {
								if(i<a.length()&&i<b.length()&&a.charAt(i)==b.charAt(i)) {
									sonuc="0"+sonuc;
								}
								else
									sonuc="1"+sonuc;
							}
							YazmacObegi[hedefYazmac]=Integer.parseInt(sonuc,2);
							break;
						case"srl":
							YazmacObegi[hedefYazmac]=Integer.rotateRight(yazmacDegeri1, yazmacDegeri2);
							break;
						case"sra":
							YazmacObegi[hedefYazmac]=Integer.rotateRight(yazmacDegeri1, yazmacDegeri2);
							break;
							
					}
					
					break;
				case "I"://I tipi buyruklar yurutme
					yazmacDegeri1=YazmacObegi[kaynakYazmac1];
					anlikDeger=immediateValue;
					// YazmacObegi[hedefYazmac];
					switch(currentBuyruk) {
					case"addi":
						YazmacObegi[hedefYazmac]=yazmacDegeri1+anlikDeger;
						break;
					case"subi":
						YazmacObegi[hedefYazmac]=yazmacDegeri1-anlikDeger;
						break;
					case"xori":
						String a =Integer.toBinaryString(yazmacDegeri1);
						String b =Integer.toBinaryString(anlikDeger);
						String sonuc="";
						for(int i=0;i<a.length()||i<b.length();i++) {
							if(a.charAt(i)==b.charAt(i)) {
								sonuc="0"+sonuc;
							}
							else
								sonuc="1"+sonuc;
						}
						YazmacObegi[hedefYazmac]=Integer.parseInt(sonuc,2);
						break;
					case"srai":
						YazmacObegi[hedefYazmac]=Integer.rotateRight(yazmacDegeri1, anlikDeger);
						break;
					case"slti":
						if(yazmacDegeri1<anlikDeger) {
							YazmacObegi[hedefYazmac]=1;
						}
						else
							YazmacObegi[hedefYazmac]=0;
						break;
					case"lw":
						YazmacObegi[hedefYazmac]=VeriBelleði[yazmacDegeri1+anlikDeger];
						break;
					case"lb":
						YazmacObegi[hedefYazmac]=VeriBelleði[yazmacDegeri1+anlikDeger];
						break;
					case"jalr":
						IncProgramSetByValue(yazmacDegeri1+anlikDeger-4);
						break;
						
						
					}
					break;
				case "S"://S tipi buyruklar yurutme
					yazmacDegeri1=YazmacObegi[kaynakYazmac1];
					yazmacDegeri2=YazmacObegi[kaynakYazmac1];
					anlikDeger=immediateValue;
					
					switch(currentBuyruk) {
					case"sw":
						VeriBelleði[yazmacDegeri1+anlikDeger]=yazmacDegeri2;
						break;
					case"sb":
						VeriBelleði[yazmacDegeri1+anlikDeger]=yazmacDegeri2;
						break;

			
					}
					break;
				case "B"://B tipi buyruklar yurutme
					yazmacDegeri1=YazmacObegi[kaynakYazmac1];
					yazmacDegeri2=YazmacObegi[kaynakYazmac2];
					anlikDeger=immediateValue;
				
					switch(currentBuyruk) {
					case"beq":
						if(yazmacDegeri1==yazmacDegeri2) {
							IncProgramCounterByValue( anlikDeger*2-4);
						}
						break;
					case"bge":
						if(yazmacDegeri1>=yazmacDegeri2) {
							IncProgramCounterByValue( anlikDeger*2-4);

						}
						break;
					case"blt":
						if(yazmacDegeri1<yazmacDegeri2) {
							IncProgramCounterByValue( anlikDeger*2-4);
						}
						else {

						}
						break;
			
					}
					
					break;
				case "J"://J tipi buyruklar yurutme
					anlikDeger=immediateValue;
					// YazmacObegi[hedefYazmac];
					switch(currentBuyruk) {
					case"jal":
						 YazmacObegi[hedefYazmac]=IncProgramCounter();// 4 arttirilmis halini yazmaca yaz

						 IncProgramCounterByValue(anlikDeger*2-8);// prog sayacini anlik kadar arttir
						 // IncProgramCounterByValue(anlikDeger*2-8);------>hem normal PC+4ü engellemek icin hem de 2 satir üstteki arttirma islemini engellemek icin -8 yapildi
					}
					
					
				
				
				}
				
			}
			
			
		}while(IncProgramCounter()<bellektekiSonAdres+16);
		
		FileWriter fw;
		try {
			fw = new FileWriter("cikti.txt");
			PrintWriter p=new PrintWriter(fw,true);
			for(int i=0;i<32;i++) {
				
				p.println("x"+i+": "+YazmacObegi[i]);
			}
			
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
	}
	private static int IncProgramCounter() {
		int temp =Integer.parseInt(programSayaci, 16)+4;
		programSayaci=Integer.toHexString(temp);
		while(programSayaci.length()<bellekAdresiBoyutu) {
			programSayaci="0"+programSayaci;
		}
		return temp;

		
	}
	private static int IncProgramCounterByValue(int value) {
		
		int temp =Integer.parseInt(programSayaci, 16)+value;
		programSayaci=Integer.toHexString(temp);
		while(programSayaci.length()<bellekAdresiBoyutu) {
			programSayaci="0"+programSayaci;
		}
		return temp;

		
	}
	private  static  void PCFirstÝnitilaize() {
		for(int i=0;i<bellekAdresiBoyutu;i++) {
			programSayaci+="0";
		}
	}
	private static int IncProgramSetByValue(int value) {
		
		int temp =value;
		programSayaci=Integer.toHexString(temp);
		while(programSayaci.length()<bellekAdresiBoyutu) {
			programSayaci="0"+programSayaci;
		}
		return temp;

		
	}
}
