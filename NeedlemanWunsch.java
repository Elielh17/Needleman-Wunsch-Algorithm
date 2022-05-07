import java.io. * ;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * The program receives a csv input file, this file will contain 2 columns: sequence1 and sequence2. 
 * The first line of the file must contain the words "Sequence1" and "Sequence2" in their respective column.
 *
 *
 * The Needleman Wunsch Algorithm 
 *
 *
 * The algorithm essentially divides a large problem (e.g. the full sequence) into a
 * series of smaller problems, and it uses the solutions to the smaller problems to find
 * an optimal solution to the larger problem. Is used for determining the similarity between two very long data streams.
 *
 *
 * @author Eliel J. Hernandez Vega 844196970
 * @version 1.0
 */
public class Proyecto1 {

    /**
     * Receives an empty matrix and two sequences and with the Needleman Wunsch algorithm creates the
     * matrix of Needleman Wunsch.
     *
     * @param matrix Empty Two dimensional int array
     * @param sequence1 String with the first sequence
     * @param sequence2 String with the second sequence
     */
    public static void NeedlemanMatrix(int[][] matrix, String sequence1, String sequence2) {
        /*Gap penalty*/
        int d = -2;

        /*Sets the penalty values in their respective place, loops through the sequences to set the values
        of the matrix, and find the score of the matrix*/
        for(int i = 0; matrix.length > i; i++) {
            for(int j = 0; matrix[i].length > j; j++) {
                int equal = 0;

                /*Equals the place matrix[0][0] to Zero*/
                if(i == 0 && j == 0) {
                    matrix[i][j] = 0;
                }
                /*Sets the gap penalties for the columns*/
                else if(j == 0) {
                    int v = matrix[i-1][j] + d;
                    matrix[i][j] = v;
                }
                /*Sets the gap penalties for the rows*/
                else if(i == 0) {
                    int v = matrix[i][j-1] + d;
                    matrix[i][j] = v;
                }
                /*Checks the max condition of Needleman to set the values of the matrix*/
                else if(i > 0 && j > 0) {
                    /*Verify if they are equal*/
                    if(sequence1.charAt(j-1) == sequence2.charAt(i-1)) {
                        equal = 1;
                    }
                    else { equal = -1;}

                    /*checks the max between: fi-1,j-1 + s(Ai,Bj) > fi,j-1+d || fi-1,j-1 + s(ai,Bj) > fi-1,j + d*/
                    if(matrix[i-1][j-1] + equal > matrix[i][j-1] + d && matrix[i-1][j-1] + equal > matrix[i-1][j] + d) {
                        matrix[i][j] = matrix[i-1][j-1] + equal;
                    }
                    /*checks the max between: fi,j-1 + d > fi-1,j*/
                    else if(matrix[i][j-1] + d > matrix[i-1][j] + d) {
                        matrix[i][j] = matrix[i][j-1] + d;
                    }
                    /*checks the max between: fi,j-1 + d > fi-1,j*/
                    else {
                        matrix[i][j] = matrix[i - 1][j] + d;
                    }
                }

            }
        }
    }

    /**
     * Receives the Needleman matrix of the two sequences and the two sequences and produces the Alignments Texts
     *
     * @param matrix Two-dimensional int array which is the Needleman matrix
     * @param sequence1 String with the first sequence
     * @param sequence2 String with the second sequence
     * @return String array with the two alignment texts
     */
    public static String[] Backtracking(int[][] matrix, String sequence1, String sequence2) {

        /*The score of the matrix which is the last number of the matrix*/
        int last = matrix[matrix.length-1][matrix[0].length-1];

        /*Indexes for matrix and empty strings for the Alignments texts*/
        int indexi = matrix.length-1;
        int indexj = matrix[0].length-1;
        String str1 ="", str2 = "";

        /*Loops through the matrix and the sequences to find the Alignment text*/
        while (true) {
            /*Break statement*/
            if(indexi-1 <= -1 || indexj-1 <= -1) break;

            /*If the characters are equal at same position of the sequences adds the character to both Alignments texts*/
            if(sequence1.charAt(indexj-1) == sequence2.charAt(indexi-1)) {
                str1 = sequence1.charAt(indexj-1) + str1;
                str2 = sequence2.charAt(indexi-1) + str2;

                indexi--;
                indexj--;
            }
            /*If Fi-1,j > Fi,j-1 && Fi-1,j > Fi-1,j-1 adds a gap to the first alignment text and the character to the second alignment text*/
            else if(matrix[indexi-1][indexj] > matrix[indexi][indexj-1] && matrix[indexi-1][indexj] > matrix[indexi-1][indexj-1]){
                str1 = "-" + str1;
                str2 = sequence2.charAt(indexi-1) + str2;
                indexi--;
            }
            /*If Fi,j-1 > Fi-1,j && Fi,j-1 > Fi-1,j-1 adds a gap to the second alignment text and the character to the first alignment text*/
            else if (matrix[indexi][indexj-1] > matrix[indexi-1][indexj] && matrix[indexi][indexj-1] > matrix[indexi-1][indexj-1]) {
                str2 = "-" + str2;
                str1 = sequence1.charAt(indexj-1) + str1;
                indexj--;
            }
            /*Else if Fi-1,j-1 higher than the other conditions adds the character to both alignment texts*/
            else {
                str1 = sequence1.charAt(indexj-1) + str1;
                str2 = sequence2.charAt(indexi-1) + str2;

                indexi--;
                indexj--;
            }
        }
        String result[] = {str1, str2};
        return result;



    }

    public static void main(String[] args) {
        /*Two empty lists for the sequences one and two*/
        ArrayList<String> sequences1 = new ArrayList<String>();
        ArrayList<String> sequences2 = new ArrayList<String>();

        /*Takes the input file*/
        Scanner sc = new Scanner(System.in);
        String file = sc.nextLine();
        BufferedReader reader = null;
        String line = "";

        /*Reads the input file*/
        try {
            /*File reader*/
            reader = new BufferedReader(new FileReader(file));
            /*Loops through the lines of the input file*/
            while ((line = reader.readLine()) != null) {
                /*Divides the two columns*/
                String[] row = line.split(",");

                /*Adds the sequences to their list*/
                sequences1.add(row[0]);
                sequences2.add(row[1]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /*Closes the file reader*/
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*Creates the result file */
        File newfile = new File("results.csv");
        FileWriter fileWriter = null;

        try {
            /*Declares the File writer*/
            fileWriter = new FileWriter(newfile);
            /*Writes the title of the columns */
            fileWriter.write("sequence1,sequence2,Aligment Text,Aligment Score \n");
        }catch (IOException e) {
            e.printStackTrace();
        }

        /*Loops through the sequences in the input file and for each two sequences in each row gets the score
        of the matrix and the alignments texts*/
        for(int i = 1; i < sequences1.size(); i++) {
            /*Empty matrix*/
            int [][] matrix = new int[sequences2.get(i).length()+1][sequences1.get(i).length()+1];

            /*creating the Needleman matrix gets the alignment score*/
            NeedlemanMatrix(matrix, sequences1.get(i), sequences2.get(i));
            int score = matrix[matrix.length-1][matrix[0].length-1];

            /*Using Backtracking with the Needleman matrix gets the alignments texts*/
            String[] result = Backtracking(matrix, sequences1.get(i), sequences2.get(i));
            String AligmentText = result[0] + " " + result[1];

            /*Writes the data to the result file*/
            try {
                fileWriter.write(sequences1.get(i)+ "," + sequences2.get(i) + "," + AligmentText + "," + Integer.toString(score) + "\n");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*Closes the file writer*/
        try {
            fileWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

