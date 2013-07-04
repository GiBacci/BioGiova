package bacci.giovanni.biogiova.alignment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * Classe contenente metodi per eseguire un allineamento tra due sequenze
 *
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public class Aligner {

    private ScoreMatrix matrix;
    private Sequence seq1;
    private Sequence seq2;
    private Alphabet alph = null;
    private double[][] scores;
    private String[][] traceBack;
    private double identity = 0.0;
    private double gapped = 0.0;
    private double score = 0.0;
    private String alignment = null;
    private AlignedSequences alignedSequences = null;
    private boolean local = false;
    private final String WARNING =
            "##############################################################"
            + System.lineSeparator()
            + "### Warning!!! There are multiple possible alignments with ###"
            + System.lineSeparator()
            + "### the same probability                                   ###"
            + System.lineSeparator()
            + "##############################################################"
            + System.lineSeparator();

    /**
     * Crea l'infrastruttura per eseguire un allineamento tra due sequenze
     *
     * @param matrix matrice di score
     * @param seq1 prima sequenza
     * @param seq2 seconda sequenza
     */
    public Aligner(ScoreMatrix matrix, Sequence seq1, Sequence seq2) 
            throws InvalidAlphabetException {
        this.seq1 = seq1;
        this.seq2 = seq2;
        if(this.seq1.getAlphabet() != this.seq2.getAlphabet()){
            throw new InvalidAlphabetException(
                    "The sequences doesn't have the same Alphabet");
        }else{
            this.alph = seq1.getAlphabet();
        }
        this.matrix = matrix;
        this.scores = new double[seq2.length() + 1][seq1.length() + 1];
        this.traceBack = new String[seq2.length() + 1][seq1.length() + 1];
        for (int i = 0; i < seq1.length(); i++) {
            this.traceBack[0][(i + 1)] = "l";
        }
        for (int j = 0; j < seq2.length(); j++) {
            this.traceBack[(j + 1)][0] = "u";
        }
    }

    /**
     * Metodo privato per calcorare il massimo valore tra i tre nell'algoritmo.
     *
     * @param diagonal lo score della casella diagonale
     * @param left lo score della casella a sinistra
     * @param up lo score della casella in alto
     * @return un dizionario con le iniziali della direzione (se sono possibili
     * più direzioni la stringa sarà composta da più lettere) ed il massimo
     * valore tra i tre
     */
    private Map<String, Double> max(double diagonal, double left, double up) {
        StringBuilder builder = new StringBuilder();
        Map<Character, Double> map = new TreeMap<>();
        Map<String, Double> res = new HashMap<>();
        map.put('d', diagonal);
        map.put('l', left);
        map.put('u', up);
        Double[] arr = map.values().toArray(new Double[0]);
        Arrays.sort(arr);
        double max = arr[arr.length - 1];
        for (Character x : map.keySet()) {
            if (max == map.get(x)) {
                builder.append(x);
            }
        }
        res.put(builder.toString(), max);
        return res;
    }

    /**
     * Metodo privato che rende la posizione della casella con il più alto
     * valore di score da dove iniziare l'algoritmo traceback nell'allineamento
     * locale.
     *
     * @return Map<String, List<Integer>> last.row = numero della colonna
     * dell'ultima riga della matrice trace back; last.col = numero della riga
     * dell'ultima colonna della matrice di score. Se non presenti più numeri
     * vuol dire che ci sono più allineamenti possibili.
     */
    private Map<String, List<Integer>> getLocalBegin() {
        Map<String, List<Integer>> beginnings = new HashMap<>();
        List<Integer> maxes = new ArrayList<>();
        final String MAXLASTROW = "last.row";
        final String MAXLASTCOL = "last.col";
        final int LASTROW = this.scores.length - 1;
        final int LASTCOL = this.scores[0].length - 1;
        double maxRow = 0.0;
        double maxCol = 0.0;

        for (int j = 0; j < this.scores[0].length; j++) {
            maxRow = Math.max(maxRow, this.scores[LASTROW][j]);
        }
        /**
         * Qui metto this.scores.lenght - 1 così l'angolo in basso a destra
         * della matrice non lo conto due volte
         */
        for (int i = 0; i < this.scores.length - 1; i++) {
            maxCol = Math.max(maxCol, this.scores[i][LASTCOL]);
        }

        if (maxRow > maxCol) {
            for (int j = 0; j < this.scores[0].length; j++) {
                if (this.scores[LASTROW][j] == maxRow) {
                    maxes.add(j);
                }
            }
            beginnings.put(MAXLASTROW, maxes);
        } else if (maxRow < maxCol) {
            for (int i = 0; i < this.scores.length - 1; i++) {
                if (this.scores[i][LASTCOL] == maxCol) {
                    maxes.add(i);
                }
            }
            beginnings.put(MAXLASTCOL, maxes);
        } else if (maxRow == maxCol) {
            List<Integer> maxesRow = new ArrayList<>();
            for (int j = 0; j < this.scores[0].length; j++) {
                if (this.scores[LASTROW][j] == maxRow) {
                    maxesRow.add(j);
                }
            }
            beginnings.put(MAXLASTROW, maxesRow);
            for (int i = 0; i < this.scores.length - 1; i++) {
                if (this.scores[i][LASTCOL] == maxCol) {
                    maxes.add(i);
                }
            }
            beginnings.put(MAXLASTCOL, maxes);
        }

        return beginnings;
    }

    /**
     * Metodo void che computa la matrice di score e la traceback matrix per
     * l'allineamento
     *
     * @throws InvalidAlphabetException Se le due sequenze non hanno un alfabeto
     * riconosciuto
     */
    public void globalAlignment() throws InvalidAlphabetException,
            DifferentLengthException{
        double gop = matrix.getGapOpenPenalty();
        double gep = matrix.getGapExtensionPenalty();

        this.scores[0][0] = 0.0;
        this.scores[0][1] = gop;
        this.scores[1][0] = gop;
        for (int j = 2; j < this.seq1.length() + 1; j++) {
            this.scores[0][j] = this.scores[0][(j - 1)] + gep;
        }
        for (int i = 2; i < this.seq2.length() + 1; i++) {
            this.scores[i][0] = this.scores[(i - 1)][0] + gep;
        }
        for (int i = 1; i < this.seq2.length() + 1; i++) {
            for (int j = 1; j < this.seq1.length() + 1; j++) {
                char a = this.seq1.getSequence()[j - 1];
                char b = this.seq2.getSequence()[i - 1];
                double d = this.scores[i - 1][j - 1] + this.matrix.getScore(a, b);
                double l = 0.0;
                double u = 0.0;
                if (this.traceBack[i][j - 1] == null
                        || !this.traceBack[i][j - 1].contains("l")) {
                    l = this.scores[i][j - 1] + gop;
                } else if (this.traceBack[i][j - 1].contains("l")) {
                    l = this.scores[i][j - 1] + gep;
                }
                if (this.traceBack[i - 1][j] == null
                        || !this.traceBack[i - 1][j].contains("u")) {
                    u = this.scores[i - 1][j] + gop;
                } else if (this.traceBack[i - 1][j].contains("u")) {
                    u = this.scores[i - 1][j] + gep;
                }
                Map<String, Double> test = this.max(d, l, u);
                String dir = test.keySet().toArray(new String[0])[0];
                double max = test.values().toArray(new Double[0])[0];
                this.traceBack[i][j] = dir;
                this.scores[i][j] = max;
                this.local = false;
            }
        }
        this.traceBack();
    }

    /**
     * Computa la score matrix e la traceback matrix secondo la logica locale.
     *
     * @throws InvalidAlphabetException Se l'alfabeto non corrisponde alle
     * sequenze
     */
    public void localAlignment() throws InvalidAlphabetException,
            DifferentLengthException{
        double gop = matrix.getGapOpenPenalty();
        double gep = matrix.getGapExtensionPenalty();

        for (int j = 0; j < this.seq1.length() + 1; j++) {
            this.scores[0][j] = 0.0;
        }
        for (int i = 0; i < this.seq2.length() + 1; i++) {
            this.scores[i][0] = 0.0;
        }

        for (int i = 1; i < this.seq2.length() + 1; i++) {
            for (int j = 1; j < this.seq1.length() + 1; j++) {
                char a = this.seq1.getSequence()[j - 1];
                char b = this.seq2.getSequence()[i - 1];
                double d = this.scores[i - 1][j - 1]
                        + this.matrix.getScore(a, b);
                double l = 0.0;
                double u = 0.0;
                if (this.traceBack[i][j - 1] == null
                        || !this.traceBack[i][j - 1].contains("l")) {
                    l = this.scores[i][j - 1] + gop;
                } else if (this.traceBack[i][j - 1].contains("l")) {
                    l = this.scores[i][j - 1] + gep;
                }
                if (this.traceBack[i - 1][j] == null
                        || !this.traceBack[i - 1][j].contains("u")) {
                    u = this.scores[i - 1][j] + gop;
                } else if (this.traceBack[i - 1][j].contains("u")) {
                    u = this.scores[i - 1][j] + gep;
                }
                Map<String, Double> test = this.max(d, l, u);
                String dir = test.keySet().toArray(new String[0])[0];
                double max = test.values().toArray(new Double[0])[0];
                this.traceBack[i][j] = dir;
                if (max < 0.0) {
                    max = 0.0;
                }
                this.scores[i][j] = max;
            }
            this.local = true;
        }
        this.traceBack();
//        for (int i = 0; i < this.seq2.length() + 1; i++) {
//            for (int j = 0; j < this.seq1.length() + 1; j++) {
//                System.out.print(String.format("%+6.1f", this.scores[i][j]));
//            }
//            System.out.println();
//        }
//        for (int i = 0; i < this.seq2.length() + 1; i++) {
//            for (int j = 0; j < this.seq1.length() + 1; j++) {
//                System.out.print(String.format("%8s", this.traceBack[i][j]));
//            }
//            System.out.println();
//        }
    }

    /**
     * Metodo privato per ottenere l'allineamento delle seqeunze e calcolare 
     * i valori di score, gap score e identity score a partire dalla matrice 
     * trace back generata con il metodo {@link globalAlignment()}
     *
     * @throws InvalidAlphabetException se le sequenze non hanno un alfabeto
     * conosciuto
     * @throws DifferentLengthException se le sequenze allineate non possiedono
     * la stessa lunghezza
     */
    private void traceBack() throws InvalidAlphabetException, 
            DifferentLengthException {
        this.identity = 0.0;
        this.gapped = 0.0;
        this.score = 0.0;
        int i = this.traceBack.length - 1;
        int j = this.traceBack[0].length - 1;
        if (this.local) {
            int colBeg = 0;
            int rowBeg = 0;
            if (this.getLocalBegin().size() > 1) {
                System.out.println(this.WARNING);
                if (i < j) {
                    List<Integer> row = this.getLocalBegin().get("last.row");
                    rowBeg = Collections.max(row);
                } else if (i > j) {
                    List<Integer> col = this.getLocalBegin().get("last.col");
                    colBeg = Collections.max(col);
                } else {
                    System.out.println(this.WARNING);
                }
            } else {
                if (this.getLocalBegin().containsKey("last.row")) {
                    List<Integer> list = this.getLocalBegin().get("last.row");
                    if (list.size() > 1) {
                        System.out.println(this.WARNING);
                    }
                    rowBeg = Collections.max(list);
                } else {
                    List<Integer> list = this.getLocalBegin().get("last.col");
                    if (list.size() > 1) {
                        System.out.println(this.WARNING);
                    }
                    colBeg = Collections.max(list);
                }
            }
            if (rowBeg != 0) {
                int r = j;
                while (r > rowBeg) {
                    this.traceBack[i][r] = "l";
                    r--;
                }
            } else if (colBeg != 0) {
                int c = i;
                while (c > colBeg) {
                    this.traceBack[c][j] = "u";
                    c--;
                }
            }
        }
        StringBuilder seq1 = new StringBuilder();
        StringBuilder seq2 = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        while (i > 0 || j > 0) {
            char[] a = this.traceBack[i][j].toCharArray();
            if (a.length > 1) {
                Map<Character, Double> map = new TreeMap<>();
                for (Character c : a) {
                    switch (c) {
                        case 'd':
                            map.put('d', this.scores[i - 1][j - 1]);
                            break;
                        case 'l':
                            map.put('l', this.scores[i][j - 1]);
                            break;
                        case 'u':
                            map.put('u', this.scores[i - 1][j]);
                            break;
                    }
                }
                Double[] doubArr = map.values().toArray(new Double[0]);
                Arrays.sort(doubArr);
                StringBuilder build = new StringBuilder();
                double max = doubArr[doubArr.length - 1];
                for (Character c : map.keySet()) {
                    double x = map.get(c);
                    if (x == max) {
                        build.append(c);
                    }
                }
                if (build.length() > 1) {
                    if (build.toString().contains("d")) {
                        seq1.append(this.seq1.getSequence()[j - 1]);
                        seq2.append(this.seq2.getSequence()[i - 1]);
                        i--;
                        j--;
                    } else if (build.toString().contains("l")
                            && this.seq1.length() > this.seq2.length()) {
                        seq1.append(this.seq1.getSequence()[j - 1]);
                        seq2.append("-");
                        j--;
                    } else if (build.toString().contains("u")
                            && this.seq1.length() < this.seq2.length()) {
                        seq1.append("-");
                        seq2.append(this.seq2.getSequence()[i - 1]);
                        i--;
                    } else {
                        System.out.println("Warning: "
                                + "There are multiple possible alignments!");
                    }
                } else {
                    char[] charArr = build.toString().toCharArray();
                    switch (charArr[0]) {
                        case 'd':
                            seq1.append(this.seq1.getSequence()[j - 1]);
                            seq2.append(this.seq2.getSequence()[i - 1]);
                            i--;
                            j--;
                            break;
                        case 'l':
                            seq1.append(this.seq1.getSequence()[j - 1]);
                            seq2.append("-");
                            j--;
                            break;
                        case 'u':
                            seq1.append("-");
                            seq2.append(this.seq2.getSequence()[i - 1]);
                            i--;
                            break;
                    }
                }
            } else {
                switch (a[0]) {
                    case 'd':
                        seq1.append(this.seq1.getSequence()[j - 1]);
                        seq2.append(this.seq2.getSequence()[i - 1]);
                        i--;
                        j--;
                        break;
                    case 'l':
                        seq1.append(this.seq1.getSequence()[j - 1]);
                        seq2.append("-");
                        j--;
                        break;
                    case 'u':
                        seq1.append("-");
                        seq2.append(this.seq2.getSequence()[i - 1]);
                        i--;
                        break;
                }
            }
        }
        Sequence s1 = new Sequence(this.alph, seq1.reverse().toString());
        Sequence s2 = new Sequence(this.alph, seq2.reverse().toString());
        
        if(s1.length() != s2.length()){
            throw new DifferentLengthException(
                    "Sequences length are not the same.");
        }
        
        List<Sequence> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        AlignedSequences sequences = new 
                AlignedSequences(this.alph, list, this.matrix);
        
        this.alignedSequences = sequences;
        
        for (int m = 0; m < s1.length(); m++) {
            if (s1.getSequence()[m] == s2.getSequence()[m]) {
                comment.append("*");
                this.identity++;
                this.score += this.matrix.getScore(
                        s1.getSequence()[m], s2.getSequence()[m]);
            } else if (s1.getSequence()[m] == '-' || 
                    s2.getSequence()[m] == '-') {
                comment.append(" ");
                this.gapped++;
                if (m > 0) {
                    if (s1.getSequence()[m - 1] == '-' || 
                            s2.getSequence()[m - 1] == '-') {
                        this.score += this.matrix.getGapExtensionPenalty();
                    } else {
                        this.score += this.matrix.getGapOpenPenalty();
                    }
                }
            } else {
                comment.append(" ");
            }
        }
        this.identity = (this.identity / (double) s1.length()) * 100;
        this.gapped = (this.gapped / (double) s1.length()) * 100;
        String s = s1 + "\n" + s2 + "\n" + comment.toString();
        this.alignment = s;
    }

    /**
     * Rende la percentuale di identità (match/lunghezza allineamento)
     *
     * @return la percentuale di identità
     */
    public double getIdentityScore() {
        return this.identity;
    }

    /**
     * Riporta l'allineamento come una stringa composta dalle due sequenze
     * allineate. Dove è presente un '*' indica un match.
     *
     * @return la Stringa che rappresenta l'allineamneto.
     */
    @Override
    public String toString() {
        return this.alignment;
    }

    /**
     * Rende la percentuale di gap (n°gap/lunghezza allineamento)
     *
     * @return la percentuale di gap
     */
    public double getGapScore() {
        return this.gapped;
    }

    /**
     * Rende il valore di Score
     *
     * @return il valore di score
     */
    public double getScore() {
        return this.score;
    }
    
    public AlignedSequences getAlignedSequence(){
        return this.alignedSequences;
    }
}
