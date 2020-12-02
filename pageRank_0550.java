/* 
Ankan Dash 0550
CS610 Data Structures and Algorithms
NJIT ID 31520550 
UCID ad892
Programming Project (PrP) -  Google’s PageRank algorithm
*/

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import static java.lang.System.*;

class pageRank_0550{
  public static void main(String[] args) throws IOException {
    DecimalFormat df = new DecimalFormat("0.0000000");
    int init_value = 0;
    int iter = 0;
    String textFile = "";
    int vertices = 0;
    int edges = 0;
    int iter_counter = 0;
    double error_rate = 0.0;
    double d = 0.85;
    boolean flag = true;
    if (args.length != 3){
      System.out.println("Use the following format : 'pageRank_0550 interations initialvalue filename'");
      return;
    }
    for (int i=0;i<args.length;i++) {
      iter = Integer.parseInt(args[0]);
      init_value = Integer.parseInt(args[1]);
      textFile = args[2];
    }
    if (!(init_value >= -2 && init_value <= 1)){
      System.out.println("Initial values can be : -2, -1, 0 or 1");
      return;
    }
    Scanner input = new Scanner(new File(textFile));
    vertices = input.nextInt();
    edges = input.nextInt();
    double graph[][] = new double[vertices][vertices];
    for(int i = 0; i < vertices; i++){
      for(int j = 0; j < vertices; j++){
            graph[i][j] = 0.0;
      }
    }
    while(input.hasNextInt()){
      graph[input.nextInt()][input.nextInt()] = 1.0;
    }
    if (iter < 0){
      error_rate = Math.pow(10, iter);
    }
    double pgrk[] = new double[vertices];
    double out_degree[] = new double[vertices];
    double initial_pgrk [] = new double[vertices];
    double sub[] = new double[vertices];
    for(int i=0; i<vertices; i++){
      out_degree[i] = 0;
      for(int j=0; j<vertices; j++){
        out_degree[i] = out_degree[i] + graph[i][j];
      }
    }
      if (vertices < 10){
        switch(init_value){
          case 0:
          for (int i=0; i<vertices;i++){
            initial_pgrk[i] = 0.0;
          }
          break;
          case 1:
          for (int i=0; i<vertices;i++){
            initial_pgrk[i] = 1.0;
          }
          break;
          case -1:
          for (int i=0; i<vertices;i++){
            initial_pgrk[i] = 1.0/vertices;
          }
          break;
          case -2:
          for (int i=0; i<vertices;i++){
            initial_pgrk[i] = 1.0/Math.sqrt(vertices);
          }
          break;
        }
      }
      else{
        iter = 0;
        init_value = -1;
        error_rate = 0.00001;
        for (int i=0; i<vertices;i++){
          initial_pgrk[i] = 1.0/vertices;
        }
      }
      System.out.print("Base: " +iter_counter + " : ");
      if (vertices > 5){
        System.out.println();
      }
      for(int i=0; i<vertices; i++){
          System.out.print(" P [" + i + "] = " + df.format(initial_pgrk[i]));
          if (vertices > 5){
            System.out.println();
        }
      }
      System.out.println();
      if(iter > 0){
        do{
          for (int j=0; j<vertices; j++){
            pgrk[j]=0.0;
          }
          for (int j=0; j<vertices; j++){
            for (int k=0; k<vertices; k++){
              if(graph[k][j]==1){
                pgrk[j] = pgrk[j]+initial_pgrk[k]/out_degree[k];
              }
            }
          }
          System.out.print("Iter: " + (iter_counter+1) + " : ");
          if (vertices > 5){
            System.out.println();
          }
          for (int j=0; j<vertices; j++){
            pgrk[j] = d*pgrk[j] + (1-d)/vertices;
            System.out.print(" P [" + j + "] = " + df.format(pgrk[j]));
            if (vertices > 5){
              System.out.println();
          }
        }
        System.out.println();
        for (int j=0; j<vertices; j++){
          initial_pgrk[j]=pgrk[j];
        }
        iter_counter++;
        iter--;
      } while(iter !=0);
    }
    else{
      do{
          if(flag == true)
          {
             flag = false;
          }
          else
          {
            for(int i = 0; i < vertices; i++) {
              initial_pgrk[i] = pgrk[i];
            }
          }
          for (int j=0; j<vertices; j++){
            pgrk[j]=0.0;
            sub[j]=0.0;
          }
          for (int j=0; j<vertices; j++){
            for (int k=0; k<vertices; k++){
              if(graph[k][j]==1){
                pgrk[j] = pgrk[j]+initial_pgrk[k]/out_degree[k];
              }
            }
          }
          System.out.print("Iter: " + (iter_counter+1) + " : ");
          if (vertices > 5){
            System.out.println();
          }
          for (int j=0; j<vertices; j++){
            pgrk[j] = d*pgrk[j] + (1-d)/vertices;
            System.out.print(" P [" + j + "] = " + df.format(pgrk[j]));
            if (vertices > 5){
              System.out.println();
          }
        }
        System.out.println();
        iter_counter++;
      } while(tests(pgrk, initial_pgrk, vertices, error_rate)!=true);
    }
  }
    public static boolean tests(double initial[], double previous[], int n, double error_rate){
       for(int i = 0 ; i < n; i++){
        if (Math.abs(initial[i]-previous[i]) > error_rate )
          return false;
        }
       return true;
    }
}
