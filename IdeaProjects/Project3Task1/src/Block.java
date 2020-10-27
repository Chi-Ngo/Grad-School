import org.json.simple.JSONObject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class Block {
    //the class Block contains these variables: index, timestamp, data, hash of previous block, nonce, difficulty
    int index;
    Timestamp timestamp;
    String data;
    String previousHash;
    BigInteger nonce = new BigInteger("0");
    int difficulty;

    //constructor for class Block
    //set up index, timestamp, data, and difficulty
    public Block(int index, java.sql.Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
    }

    //method to calculate the hash of the data of a block
    public String calculateHash() {
        //concatenate all the data into a string s
        String s = index + timestamp.toString() + data
                + previousHash + nonce.toString() + difficulty;
        MessageDigest md = null;
        //use MessageDigest to find a byte array of the hash using the appropriate type.
        //First, instantiate MessageDigest with the type of hash (MD5/SHA-256)
        //Then, use update() to find a byte array representation of the hash
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(s.getBytes());
        byte[] value = md.digest();
        //create a hexString to store the hash as hexadecimal form with all leading zeros
        StringBuffer hexString = new StringBuffer();
        //loop through each byte in the array and convert it to the appropriate string
        for (int i = 0; i < value.length; i++) {
            String hex = Integer.toHexString(0xff & value[i]);
            if(hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }
        //return the hash as a string in upper case
        return hexString.toString().toUpperCase();
    }

    //getter for nonce, return nonce
    public BigInteger getNonce() {
        return nonce;
    }

    //method to calculate the proof of work of a block according to a predefined difficulty
    public String proofOfWork() {
        boolean done = false;
        String check;
        //check proof while process is not done
        do {
            //calculate the hash of the block
            check = calculateHash();
            //convert the hash string to a character array
            char[] work = check.toCharArray();
            //variable to store the number of leading zeros
            int count = 0;
            //loop through each character in the hash string, if it's zero, increase count by 1
            //break the loop when we finish counting the number of leading zeros
            int i = 0;
            while (work[i] == '0') {
                //increase count if the character is zeros
                count += 1;
                i += 1;
            }
            //if the number of leading zeros is at least difficulty, proof of work is done
            //else, increase nonce of the block by 1 and repeat the process
            if (count >= difficulty) {
                done = true;
            } else {
                nonce = nonce.add(new BigInteger("1"));
            }
        } while (!done);
        //return the hash that results in the proof of work
        return check;
    }

    //getter for difficulty, return difficulty
    public int getDifficulty() {
        return difficulty;
    }

    //setter for difficulty to the value of the parameter
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    //convert the block data to a JSON string representation, Override java default toString() method
    @Override
    public String toString() {
        //create a LinkedHashMap object to preserve the input order
        Map obj = new LinkedHashMap();
        //put in all the information of the block with the appropriate key
        obj.put("index", index);
        obj.put("time stamp", timestamp.toString());
        obj.put("Tx", data);
        obj.put("PrevHash", previousHash);
        obj.put("nonce", nonce.toString());
        obj.put("difficulty", difficulty);
        //convert the map object to a JSON string
        String json = JSONObject.toJSONString(obj);
        //return this JSON string representation
        return json;
    }

    //setter for previousHash, set to the value of the parameter
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    //getter for previousHash, return the hash of the previous block
    public String getPreviousHash() {
        return previousHash;
    }

    //getter for the index of the block, return index
    public int getIndex() {
        return index;
    }

    //setter for the index of the block, set to the value of the parameter
    public void setIndex(int index) {
        this.index = index;
    }

    //setter for timestamp of the block, set to the value of the parameter
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    //getter for timestamp of the block, return timestamp
    public Timestamp getTimestamp() {
        return timestamp;
    }

    //getter for data of the block, return the data
    public String getData() {
        return data;
    }

    //setter for data of the block, set data to the value of the parameter
    public void setData(String data) {
        this.data = data;
    }
}
