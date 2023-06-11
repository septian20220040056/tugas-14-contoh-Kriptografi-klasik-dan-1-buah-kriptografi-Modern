import javax.crypto.*;
import java.security.*;

public class Main {
    public static void main(String[] args) {
        String keyword = "my_password_123";

        // -------------------------------- modern
        // aes encription
        System.out.println("MODERN ENCRIPTION");
        try {
            String aesEncription = aesEncription(keyword);
            System.out.println("AES ENCRIPTION : " + aesEncription);
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }

        // -------------------------------- klasik
        System.out.println();
        System.out.println("KLASIK ENCRIPTION");

        // 1. Caesar Cipher
        String chaesarExample = caesarCipherEncription(keyword, 3);
        System.out.println("Caesar Cipher : " + chaesarExample);

        // 2. Vigenère Cipher
        String vigenereChiperEncription = vigenereCipherEncription(keyword, "KEY");
        System.out.println("Vigenère Cipher : " + vigenereChiperEncription);

        // 3. Hill Chiper
        String hillChiperEncription = hillChiperEncription(keyword);
        System.out.println("Hill Chiper : " + hillChiperEncription);
    }

    // ----------- modern encription ------------
    // AES
    public static String aesEncription(String text) throws NoSuchPaddingException , IllegalBlockSizeException , NoSuchAlgorithmException , BadPaddingException , InvalidKeyException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();

        // Create cipher object and set encryption mode
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encrypt the plaintext
        byte[] encryptedText = cipher.doFinal(text.getBytes());
        return new String(encryptedText);
    }

    // ----------- klasik encription ------------
    // Caesar Cipher
    public static String caesarCipherEncription(String plainText, int key) {
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char ch = plainText.charAt(i);

            if (Character.isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    char encryptedCh = (char) (((ch - 'A') + key) % 26 + 'A');
                    encryptedText.append(encryptedCh);
                } else {
                    char encryptedCh = (char) (((ch - 'a') + key) % 26 + 'a');
                    encryptedText.append(encryptedCh);
                }
            } else {
                encryptedText.append(ch);
            }
        }

        return encryptedText.toString();
    }

    // Vigenère Cipher
    public static String vigenereCipherEncription(String text, String key) {
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (Character.isLetter(ch)) {
                char encryptedCh = (char) (((ch - 'A') + (key.charAt(j) - 'A')) % 26 + 'A');
                encryptedText.append(encryptedCh);
                j = (j + 1) % key.length();
            } else {
                encryptedText.append(ch);
            }
        }

        return encryptedText.toString();
    }

    // Hill Chiper
    private static final int MATRIX_SIZE = 2; // Ukuran matriks kunci
    public static String hillChiperEncription(String plainText) {
        // Mengubah plain text menjadi bentuk yang sesuai dengan matriks kunci
        plainText = formatPlainText(plainText);
        int[][] keyMatrix = {{2, 3}, {1, 4}}; // Matriks kunci
        // Memecah plain text menjadi pasangan huruf
        String[] letterPairs = splitIntoPairs(plainText);

        // Enkripsi setiap pasangan huruf menggunakan matriks kunci
        StringBuilder encryptedText = new StringBuilder();
        for (String pair : letterPairs) {
            int[] letterIndices = getLetterIndices(pair);
            int[] encryptedIndices = multiplyMatrix(keyMatrix, letterIndices);
            String encryptedPair = getPairFromIndices(encryptedIndices);
            encryptedText.append(encryptedPair);
        }

        return encryptedText.toString();
    }

    // Mengubah plain text menjadi bentuk yang sesuai dengan matriks kunci
    private static String formatPlainText(String plainText) {
        plainText = plainText.replaceAll("\\s+", ""); // Menghapus spasi
        plainText = plainText.toUpperCase(); // Mengubah huruf menjadi uppercase
        if (plainText.length() % MATRIX_SIZE != 0) {
            // Menambahkan huruf 'X' jika panjang plain text tidak sesuai dengan ukuran matriks kunci
            int paddingLength = MATRIX_SIZE - (plainText.length() % MATRIX_SIZE);
            plainText += "X".repeat(paddingLength);
        }
        return plainText;
    }

    // Memecah plain text menjadi pasangan huruf
    private static String[] splitIntoPairs(String plainText) {
        String[] letterPairs = new String[plainText.length() / MATRIX_SIZE];
        for (int i = 0; i < plainText.length(); i += MATRIX_SIZE) {
            letterPairs[i / MATRIX_SIZE] = plainText.substring(i, i + MATRIX_SIZE);
        }
        return letterPairs;
    }

    // Mengembalikan indeks huruf berdasarkan ASCII
    private static int[] getLetterIndices(String pair) {
        int[] letterIndices = new int[MATRIX_SIZE];
        for (int i = 0; i < pair.length(); i++) {
            char ch = pair.charAt(i);
            letterIndices[i] = ch - 'A';
        }
        return letterIndices;
    }

    // Melakukan perkalian matriks antara matriks kunci dan indeks huruf
    private static int[] multiplyMatrix(int[][] keyMatrix, int[] letterIndices) {
        int[] encryptedIndices = new int[MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            int sum = 0;
            for (int j = 0; j < MATRIX_SIZE; j++) {
                sum += keyMatrix[i][j] * letterIndices[j];
            }
            encryptedIndices[i] = sum % 26;
        }
        return encryptedIndices;
    }

    // Mengembalikan pasangan huruf dari indeks
    private static String getPairFromIndices(int[] indices) {
        StringBuilder pair = new StringBuilder();
        for (int index : indices) {
            char ch = (char) (index + 'A');
            pair.append(ch);
        }
        return pair.toString();
    }

}