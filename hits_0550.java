/* 
Ankan Dash 0550
CS610 Data Structures and Algorithms
NJIT ID 31520550 
UCID ad892
Programming Project (PrP) - Kleinberg’s HITS Algorithm
*/
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import static java.lang.System.*;

class hits_0550{
  public static void main(String[] args) throws Exception {
    DecimalFormat df = new DecimalFormat("0.0000000");
    int int_value = 0;
    int iter = 0;
    String textFile = "";
    int vertices = 0;
    int edges = 0;
    int iter_counter = 0;
    double error_rate = 0.0;
    if (args.length != 3){
      System.out.println("Use the following format : 'hits_0550 interations initialvale filename'");
      return;
    }
    for (int i=0;i<args.length;i++) {
      iter = Integer.parseInt(args[0]);
      int_value = Integer.parseInt(args[1]);
      textFile = args[2];
    }
    if (!(int_value >= -2 && int_value <= 1)){
      System.out.println("initial values can be : -2, -1, 0 or 1");
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

    double auth[][]= new double[vertices][1];
    double hub[][] = new double[vertices][1];
    double auth_sum = 0.0;
    double hub_sum = 0.0;
    double tran_graph[][] = new double[vertices][vertices];
    double initial_auth[][] = new double[vertices][1];
    double initial_hub[][] = new double[vertices][1];
    double auth_prev[][] = new double [vertices][1];
    double hub_prev [][] = new double [vertices][1];
    double scaler_auth = 0;
    double scaler_for_hub = 0;

    for(int i=0;i<vertices;i++){
      for(int j=0; j<vertices; j++){
        tran_graph[i][j] = graph[j][i];
      }
    }
    
    if (vertices < 10){
      switch(int_value){
        case 0:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_auth[i][j] = 0.0;
              initial_hub[i][j] = 0.0;
            }
          }
          break;
          case 1:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_auth[i][j] = 1.0;
              initial_hub[i][j] = 1.0;
            }
          }
          break;
          case -1:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_auth[i][j] = 1.0/vertices;
              initial_hub[i][j] = 1.0/vertices;
            }
          }
          break;
          case -2:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_auth[i][j] = 1.0/Math.sqrt(vertices);
              initial_hub[i][j] = 1.0/Math.sqrt(vertices);
            }
          }
          break;
      }
    }
   
    else {
      iter = 0;
      int_value = -1;
      error_rate = 0.00001;
      for(int i=0; i<vertices; i++){
        for(int j=0; j<1; j++){
          initial_auth[i][j] = 1.0/vertices;
          initial_hub[i][j] = 1.0/vertices;
        }
      }
    }
  
    for (int i=0; i<vertices; i++){
      for (int j=0; j<1; j++){
        for (int k=0; k<vertices; k++){
          auth_sum = auth_sum + tran_graph[i][k]*initial_hub[k][j];
        }
        auth[i][j] = auth_sum;
        auth_sum = 0;
      }
    }
    
    for (int i=0; i<vertices; i++){
      for (int j=0; j<1; j++){
        for (int k=0; k<vertices; k++){
          hub_sum = hub_sum + graph[i][k]*auth[k][j];
        }
        hub[i][j] = hub_sum;
        hub_sum = 0;
      }
    }
    
    for (int i=0; i<vertices; i++ ){
      for (int j=0; j<1; j++){
        auth_sum = auth_sum+ (auth[i][j]*auth[i][j]);
      }
    }
  
    for (int i=0; i<vertices; i++ ){
      for (int j=0; j<1; j++){
        hub_sum = hub_sum + (hub[i][j]*hub[i][j]);
      }
    }
    System.out.print("Base: " +iter_counter + " : ");
    if (vertices > 5){
      System.out.println();
    }
    for(int i=0; i<vertices; i++){
      for(int j=0; j<1; j++){
        System.out.print("A/H [" + i + "] = " + df.format(initial_auth[i][j]) + "/" + df.format(initial_hub[i][j]) + " ");
        if (vertices > 5){
          System.out.println();
        }
      }
    }
    System.out.println();
    
    if(iter > 0){
      while (iter != 0){
        System.out.print("Iter: " + (iter_counter+1) + " :  ");
        if (vertices > 5){
          System.out.println();
        }
        for (int i=0; i<vertices; i++){
          for (int j=0; j<1; j++){
            
            scaler_auth = Math.sqrt(auth_sum);
            scaler_for_hub = Math.sqrt(hub_sum);
            auth[i][j] = auth[i][j]/scaler_auth;
            hub[i][j] = hub[i][j]/scaler_for_hub;
            System.out.print(" A/H [" + i + "] = " + df.format(auth[i][j]) + "/" + df.format(hub[i][j]) + " ");
            if (vertices > 5){
              System.out.println();
            }
          }
        }
        
        auth_sum = 0;
        hub_sum = 0;
        for (int a=0; a<vertices; a++){
          for (int b=0; b<1; b++){
            for (int c=0; c<vertices; c++){
              auth_sum = auth_sum + tran_graph[a][c]*hub[c][b];
            }
            auth[a][b] = auth_sum;
            auth_sum = 0;
          }
        }
        for (int p=0; p<vertices; p++){
          for (int q=0; q<1; q++){
            for (int r=0; r<vertices; r++){
              hub_sum = hub_sum + graph[p][r]*auth[r][q];
            }
            hub[p][q] = hub_sum;
            hub_sum = 0;
          }
        }
        for (int s=0; s<vertices; s++ ){
          for (int t=0; t<1; t++){
            auth_sum = auth_sum+ (auth[s][t]*auth[s][t]);
          }
        }
        for (int x=0; x<vertices; x++ ){
          for (int y=0; y<1; y++){
            hub_sum = hub_sum + (hub[x][y]*hub[x][y]);
          }
        }
        System.out.println();
        iter = iter - 1;
        iter_counter++;
      }
    }
    else{
      do {
        for (int i = 0; i<vertices; i++){
          for (int j = 0; j<1; j++){
            auth_prev[i][j] = auth[i][j];
            hub_prev[i][j] = hub[i][j];
          }
        }
        System.out.print("Iter: " + (iter_counter+1) + " : ");
        if (vertices > 5){
          System.out.println();
        }
        for (int i=0; i<vertices; i++){
          for (int j=0; j<1; j++){
            
            scaler_auth = Math.sqrt(auth_sum);
            scaler_for_hub = Math.sqrt(hub_sum);
            auth[i][j] = auth[i][j]/scaler_auth;
            hub[i][j] = hub[i][j]/scaler_for_hub;
            System.out.print("A/H [" + i + "] = " + df.format(auth[i][j]) + "/" + df.format(hub[i][j]) + " ");
            if (vertices > 5){
              System.out.println();
            }
          }
        }
      
        auth_sum = 0;
        hub_sum = 0;
        for (int a=0; a<vertices; a++){
          for (int b=0; b<1; b++){
            for (int c=0; c<vertices; c++){
              auth_sum = auth_sum + tran_graph[a][c]*hub[c][b];
            }
            auth[a][b] = auth_sum;
            auth_sum = 0;
          }
        }
        for (int p=0; p<vertices; p++){
          for (int q=0; q<1; q++){
            for (int r=0; r<vertices; r++){
              hub_sum = hub_sum + graph[p][r]*auth[r][q];
            }
            hub[p][q] = hub_sum;
            hub_sum = 0;
          }
        }
        for (int s=0; s<vertices; s++ ){
          for (int t=0; t<1; t++){
            auth_sum = auth_sum+ (auth[s][t]*auth[s][t]);
          }
        }
        for (int x=0; x<vertices; x++ ){
          for (int y=0; y<1; y++){
            hub_sum = hub_sum + (hub[x][y]*hub[x][y]);
          }
        }
        System.out.println();
        iter_counter++;
      } while (false == tests(auth, auth_prev, vertices, error_rate) || false == tests(hub, hub_prev, vertices,error_rate));
    }
  }
  
  public static boolean tests(double initial[][], double previous[][], int n, double error_rate){
     for(int i = 0 ; i < n; i++){
       for (int j = 0; j < 1; j++){
         if ( Math.abs(initial[i][j] - previous[i][j]) > error_rate )
           return false;
       }
     }
     return true;
  }
}
