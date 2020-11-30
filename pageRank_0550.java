/* 
Ankan Dash 
CS610 Data Structures and Algorithms
NJIT ID 31520550 
UCID ad892
PRP -  PageRank
*/

import java.util.*;
import java.io.*;
import static java.lang.System.*;
import java.text.DecimalFormat;
class pageRank_0550{
  public static void main(String[] args) throws IOException {
    DecimalFormat df = new DecimalFormat("0.0000000");
    int initial_value = 0;
    int iterations = 0;
    String TextFileName = "";
    int vertices = 0;
    int edges = 0;
    int iteration_counter = 0;
    double errorrate = 0.0;
    double d = 0.85;
    boolean flag = true;
    if (args.length != 3){
      System.out.println("Use the following format : 'pageRank_0550 interations initialvalue filename'");
      return;
    }
    for (int i=0;i<args.length;i++) {
      iterations = Integer.parseInt(args[0]);
      initial_value = Integer.parseInt(args[1]);
      TextFileName = args[2];
    }
    if (!(initial_value >= -2 && initial_value <= 1)){
      System.out.println("Enter one of the following initial values : -2, -1, 0 or 1");
      return;
    }
    Scanner scanner = new Scanner(new File(TextFileName));
    vertices = scanner.nextInt();
    edges = scanner.nextInt();
    double graph[][] = new double[vertices][vertices];
    for(int i = 0; i < vertices; i++){
      for(int j = 0; j < vertices; j++){
            graph[i][j] = 0.0;
      }
    }
    while(scanner.hasNextInt()){
      graph[scanner.nextInt()][scanner.nextInt()] = 1.0;
    }
    if (iterations < 0){
      errorrate = Math.pow(10, iterations);
    }
    double out_degree[] = new double[vertices];
    double initial_pgrk [] = new double[vertices];
    double pgrk[] = new double[vertices];
    double sub[] = new double[vertices];
    for(int i=0; i<vertices; i++){
      out_degree[i] = 0;
      for(int j=0; j<vertices; j++){
        out_degree[i] = out_degree[i] + graph[i][j];
      }
    }
      if (vertices < 10){
        switch(initial_value){
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
        iterations = 0;
        initial_value = -1;
        errorrate = 0.00001;
        for (int i=0; i<vertices;i++){
          initial_pgrk[i] = 1.0/vertices;
        }
      }
      System.out.print("Base: " +iteration_counter + " : ");
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
      if(iterations > 0){
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
          System.out.print("Iter: " + (iteration_counter+1) + " : ");
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
        iteration_counter++;
        iterations--;
      } while(iterations !=0);
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
          System.out.print("Iter: " + (iteration_counter+1) + " : ");
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
        iteration_counter++;
      } while(ConvergenceTest8699(pgrk, initial_pgrk, vertices, errorrate)!=true);
    }
  }
    public static boolean ConvergenceTest8699(double initial[], double previous[], int n, double errorrate){
       for(int i = 0 ; i < n; i++){
        if (Math.abs(initial[i]-previous[i]) > errorrate )
          return false;
        }
       return true;
    }
}
