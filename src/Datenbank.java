package src;

import src.View_GUI.ViewManager;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

import static java.nio.charset.StandardCharsets.*;

/**
 * "Datenbank" mit overengineered encryption
 * <p>
 * Beim Einlogen wird der alte Kontostand wieder aufgenommen
 * Speicherung in Datei
 * Läd den Kontostand
 */
public class Datenbank {
    private String unhashed;
    private String hashed;

    private int money = 0;

    private int userPos = -1;

    private Path FILE;

    private static Datenbank instance;

    public static Datenbank getInstance() {
        if (instance == null) {
            instance = new Datenbank();
        }
        return instance;
    }

    private Datenbank() {
        try {
            Path storageDir = Paths.get(System.getProperty("user.home"), ".javino");
            Files.createDirectories(storageDir);
            FILE = storageDir.resolve("DATA");
        } catch (IOException e) {
            ViewManager.getInstance().displayErrorMessage("Anmeldung war nicht erfolgreich!");
        }
    }

    public boolean signUp(String username, String password) {
        // get hash of username + password
        // so that they don't have to be stored as clear text
        unhashed = username + " " + password;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((unhashed).getBytes(UTF_8));
            hashed = encode(hash);
        } catch (NoSuchAlgorithmException ignored) {
        }

        // and check if hash exists
        // yes: compare hashes and if successful, return false (user already exists)
        // no: add user and return true
        // encryption by: https://www.baeldung.com/java-aes-encryption-decryption
        try {
            Scanner scanner = new Scanner(FILE);
            boolean userExists = false;
            while (scanner.hasNextLine()) {
                userPos++;
                String[] info = scanner.nextLine().split(":");
                String hash = info[0];
                if (hash.equals(hashed)) {
                    userExists = true;
                    break;
                }
            }
            scanner.close();
            if (userExists) {
                System.err.println("Couldn't sign up: User already exists.");
                userPos = -1; // damit updateMoney() beim Schließen das Geld nicht resetten kann
                return false;
            }
        } catch (IOException ignored) {}

        // add user to file
        final int startMoney = 250;
        try {
            FileWriter writer = new FileWriter(FILE.toFile(), true);
            GCMParameterSpec spec = generateIV();
            SecretKey key = getKeyFromPassword(unhashed);
            writer.write(hashed + ":" + encode(spec.getIV()) + ":" + encrypt(String.valueOf(startMoney), key, spec) + System.lineSeparator());
            writer.close();
            System.out.println("File successfully created/updated!");
            money = startMoney;
            userPos = userPos == -1 ? 0 : userPos + 1; // kp, random bug
            return true;
        } catch (IOException e) {
            System.err.println("Invalid File!");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException ex) {
            throw new RuntimeException(ex);
        }
        userPos = -1;
        return false;
    }

    public boolean signIn(String username, String password) {
        // get hash of username + password
        // so that they don't have to be stored as clear text
        unhashed = username + " " + password;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((unhashed).getBytes(UTF_8));
            hashed = encode(hash);
        } catch (NoSuchAlgorithmException ignored) {}

        // check if FILE exists and check if hash exists
        // yes: compare hashes and if successful, read data with unhashed key
        // no: create FILE and add hash
        // encryption by: https://www.baeldung.com/java-aes-encryption-decryption
        try {
            Scanner scanner = new Scanner(FILE);
            boolean userExists = false;
            while (scanner.hasNextLine()) {
                userPos++;
                String[] info = scanner.nextLine().split(":");
                String hash = info[0];
                if (hash.equals(hashed)) {
                    // get stored money
                    System.out.println("User found!");
                    userExists = true;
                    GCMParameterSpec spec = new GCMParameterSpec(128, decode(info[1]));
                    SecretKey key = getKeyFromPassword(unhashed);
                    money = Integer.parseInt(decrypt(info[2], key, spec));
                    break;
                }
            }
            scanner.close();
            if (!userExists) {
                System.err.println("User couldn't be found. Are credentials wrong?");
                userPos = -1;
                return false;
            }
            return true;
        } catch (IOException e) {
            System.err.println("File doesn't exist!");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
        userPos = -1;
        return false;
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void updateMoney(int newValue) {
        // wenn es den Spieler in der Liste noch nicht gibt
        if (userPos == -1) {
            System.out.println("Invalid User!");
            return;
        }
        // sonst: in seiner Zeile den Money-Wert aktualisieren
        try {
            Scanner scanner = new Scanner(FILE);
            StringBuilder newContent = new StringBuilder();

            int currentLine = -1;
            while (scanner.hasNextLine()) {
                currentLine++;
                if (userPos == currentLine) {
                    String[] old = scanner.nextLine().split(":");
                    GCMParameterSpec spec = new GCMParameterSpec(128, decode(old[1]));
                    SecretKey key = getKeyFromPassword(unhashed);
                    newContent.append(old[0] + ":" + old[1] + ":" + encrypt(String.valueOf(newValue), key, spec) + System.lineSeparator());
                } else {
                    newContent.append(scanner.nextLine() + System.lineSeparator());
                }
            }
            scanner.close();

            FileWriter writer = new FileWriter(FILE.toFile());
            writer.write(newContent.toString());
            writer.close();
            System.out.println("File successfully updated!");
            money = newValue;
        } catch (IOException e) {
            System.err.println("Invalid File!");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getMoney() {
        return money;
    }


    // IGNORE: helper methods

    private static GCMParameterSpec generateIV() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(128, iv);
    }

    private static SecretKey getKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), "casino".getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static String encrypt(String input, SecretKey key, GCMParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(input.getBytes());
        System.out.println(input);
        return encode(encrypted);
    }

    private static String decrypt(String input, SecretKey key, GCMParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decrypted = cipher.doFinal(decode(input));
        String str = new String(decrypted);
        System.out.println(str);
        return str;
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

}
