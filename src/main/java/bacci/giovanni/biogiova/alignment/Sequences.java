package bacci.giovanni.biogiova.alignment;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Classe per leggere una serie di sequenze direttamente da un file o da una
 * lista. Legge file in formato FASTA per adesso.
 *
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public class Sequences implements Iterator<Sequence> {

    private Path input = null;
    private Scanner scan = null;
    private Scanner scanSupport = null;
    private boolean isList = false;
    private List<Sequence> list = null;
    private Iterator<Sequence> listIt = null;
    private Alphabet alph = null;
    private int index = 0;
    private final String FASTA_DELIMITER = ">";
    private final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Costriusce un iteratore di sequenze dato un Path.
     *
     * @param input il path del file di sequenze (FASTA)
     * @throws IOException
     */
    public Sequences(Path input, Alphabet alph) throws IOException {
        this.input = input;
        this.alph = alph;
        this.scan = new Scanner(input).useDelimiter(FASTA_DELIMITER);
        this.scanSupport = new Scanner(input).useDelimiter(FASTA_DELIMITER);
        this.isList = false;
    }

    /**
     * Costruisce un iteratore di sequenze data una lista di sequenze.
     *
     * @param list la lista di sequenze
     * @param alph l'alfabeto con il quale sono costruite le sequenze
     */
    public Sequences(List<Sequence> list, Alphabet alph) {
        this.list = list;
        this.listIt = list.iterator();
        this.alph = alph;
        this.isList = true;
    }

    /**
     * Rende falso se nel file non ci sono più sequenze fasta da leggere
     *
     * @return boolean
     */
    @Override
    public boolean hasNext() {
        boolean hasNext = true;
        if (this.isList) {
            if (!this.listIt.hasNext()) {
                hasNext = false;
            }
        } else {
            if (!this.scan.hasNext()) {
                hasNext = false;
            }
        }
        return hasNext;
    }

    /**
     * Rende la prima o la successiva sequenza in un file di sequenze. Rende
     * null nel caso in cui si verifichino errori nella conversione tra stringa
     * e sequenza
     *
     * @return una {@link Sequence}
     */
    @Override
    public Sequence next() {
        Sequence seq = null;
        if (this.isList) {
            seq = listIt.next();
            this.index++;
        } else {
            String line = FASTA_DELIMITER + scan.next();
            this.index++;
            String[] lines = line.split(LINE_SEPARATOR);
            String id = null;
            StringBuilder bases = new StringBuilder();
            for (String x : lines) {
                if (x.startsWith(FASTA_DELIMITER)) {
                    id = x;
                } else {
                    bases.append(x);
                }
            }
            try {
                seq = new Sequence(this.alph, bases.toString(), id);
            } catch (InvalidAlphabetException iaE) {
            }
        }
        return seq;
    }

    /**
     * Non fa niente
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Rriporta lo scanner all'inizio del file o la lista all'inizio.
     *
     * @throws IOException
     */
    public void reset() {
        if (this.isList) {
            this.listIt = list.iterator();
            this.index = 0;
        } else {
            try {
                this.scan =
                        new Scanner(this.input).useDelimiter(FASTA_DELIMITER);
                this.scanSupport =
                        new Scanner(input).useDelimiter(FASTA_DELIMITER);
                this.index = 0;
            } catch (IOException ioE) {
            }
        }
    }

    /**
     * Conta tutte le sequenze contenute nel file. Ogni volta che si lancia il
     * metodo, questo riparte dall'inizio. Non modifica il punto dove era
     * arrivato nel file.
     *
     * @return un long che rappresenta il numero di sequenze nel file di input
     */
    public int size() {
        int count = 0;
        if (this.isList) {
            count = this.list.size();
        } else {
            while (this.scanSupport.hasNext()) {
                this.scanSupport.next();
                count++;
            }
        }
        return count;
    }

    /**
     * Rende la sequenza corrispondente all'index scelto. Le regole sono le 
     * stesse degli indici di qualsiasi altra collezione o array.
     * 
     * @param seqNum
     * @return
     * @throws IndexOutOfBoundsException
     */
    public Sequence get(int index) throws IndexOutOfBoundsException {
        Sequence seq = null;
        if (this.isList) {
            seq = this.list.get(index);
        } else {
            int oldIndex = this.index;
            if (this.index > index || !this.hasNext()) {
                this.reset();
            }
            while (this.hasNext()) {
                if (this.index == index) {
                    seq = this.next();
                    break;
                } else {
                    this.next();
                    continue;
                }
            }
            this.reset();
            while (this.index != oldIndex) {
                this.next();
            }
            if (seq == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return seq;
    }

    /**
     * Rende l'index a cui si è arrivati Iterando.
     * Es: index = 2 ----> se si invoca il metodo next(), questo renderà la
     * sequenza corrispondente all'index 2 ovvero la stessa derivante dal 
     * metodo .get(2).
     * 
     * @return 
     */
    public int getIndex() {
        return this.index;
    }
}
