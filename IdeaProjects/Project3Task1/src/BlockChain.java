import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BlockChain {
    //the class BlockChain an arraylist to store all the blocks
    //and a String recentHash to store the hash of the most recently added block
    List<Block> blocks;
    String recentHash;

    //constructor for the class BlockChain, set up an empty arraylist and empty recentHash string
    public BlockChain() {
        blocks = new ArrayList<>();
        recentHash = "";
    }

    //getter for the current system time
    public Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    //return the most recently added block in the list
    public Block getLatestBlock() {
        return blocks.get(blocks.size() - 1);
    }

    //return the number of blocks in the list
    public int getChainSize() {
        return blocks.size();
    }

    //calculate the number of hashes per second by the running machine
    public int hashesPerSecond() {
        //count stored the number of hashes done by the machine in the specified period of time
        int count = 0;
        //String to hash on
        String s = "00000000";
        //get the current system time
        long current = System.currentTimeMillis();
        //set end to be 1 second from current time
        long end = current + 1000;
        //let the loop runs for 1 second
        while (System.currentTimeMillis() < end) {
            //hash string s using SHA-256
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(s.getBytes());
            md.digest();
            //add 1 to count after every successful hash
            count += 1;
        }
        //return the number of hashes in 1 second
        return count;
    }

    //this method adds a new block to the list
    //it also updates recent hash
    public void addBlock(Block newBlock) {
        //add the new block to the arraylist blocks
        blocks.add(newBlock);
        //the hash of the block added before this new block is now this new block's previous hash (hash of the block that comes before)
        newBlock.setPreviousHash(recentHash);
        //recentHash is the new block's proof of work
        recentHash = newBlock.proofOfWork();
    }

    //this method returns a JSON representation of the entire chain
    //I use this instead of the JSON lib as this is easier to view
    @Override
    public String toString() {
        //the key is ds_chain
        String result = "{\"ds_chain\" : [ ";
        //the value is all the blocks on the chain in an array form
        //each block is on a new line
        for (Block b : blocks) {
            result += b.toString();
            result += ",\n";
        }
        //the key is chainHash, the value is recentHash
        result = result + "], \"chainHash\" : " + "\"" + recentHash + "\"}";
        //return this JSON string
        return result;
    }

    //check if a chain is valid (if each block in the chain hashes with the right number of leading zeros)
    public boolean isChainValid() {
        //check each block in the chain
        for (int i = 0; i < blocks.size(); i++) {
            //calculate the hash of the block at index i
            String check = blocks.get(i).calculateHash();
            //count the number of leading zeros it has using the helper method getZeros
            int count = getZeros(check);
            //if this is not the latest block on the chain
            if (i < blocks.size() - 1) {
                //if the number of leading zeros isn't at least the specified difficulty
                //or if the hash of this block does not match the previousHash field of the next block
                if (count < blocks.get(i).getDifficulty() || !check.equals(blocks.get(i + 1).getPreviousHash())) {
                    //chain is not valid
                    return false;
                }
            } else {
                //if the number of leading zeros isn't at least the specified difficulty
                //or if the hash does not match the stored hash of the latest added block
                if (count < blocks.get(i).getDifficulty() || !check.equals(recentHash)) {
                    //chain is not valid
                    return false;
                }
            }
        }
        //if all blocks are valid, the entire chain is valid, return true
        return true;
    }

    //check if a chain is valid (if each block in the chain hashes with the right number of leading zeros)
    public String[] invalidInfo() {
        String[] info = new String[2];
        //check each block in the chain
        for (int i = 0; i < blocks.size(); i++) {
            //calculate the hash of the block at index i
            String check = blocks.get(i).calculateHash();
            //count the number of leading zeros it has using the helper method getZeros
            int count = getZeros(check);
            //if this is not the latest block on the chain
            if (i < blocks.size() - 1) {
                //if the number of leading zeros isn't at least the specified difficulty
                //or if the hash of this block does not match the previousHash field of the next block
                if (count < blocks.get(i).getDifficulty() || !check.equals(blocks.get(i + 1).getPreviousHash())) {
                    //get the string of required leading zeros based on difficulty
                    String zeros = "0";
                    while (zeros.length() < blocks.get(i).getDifficulty()) {
                        zeros += "0";
                    }
                    //info[0] stores the index of the invalid node
                    info[0] = String.valueOf(i);
                    //info[1] stores the required leading zeros string
                    info[1] = zeros;
                }
            } else {
                //if the number of leading zeros isn't at least the specified difficulty
                //or if the hash does not match the stored hash of the latest added block
                if (count < blocks.get(i).getDifficulty() || !check.equals(recentHash)) {
                    //get the string of required leading zeros based on difficulty
                    String zeros = "0";
                    while (zeros.length() < blocks.get(i).getDifficulty()) {
                        zeros += "0";
                    }
                    //info[0] stores the index of the invalid node
                    info[0] = String.valueOf(i);
                    //info[1] stores the required leading zeros string
                    info[1] = zeros;
                }
            }
        }
        return info;
    }

    //helper method to count the number of leading zeros of a string
    int getZeros(String hash) {
        //convert the string to a char array
        char[] work = hash.toCharArray();
        int count = 0;
        //loop through each character in the hash string, if it's zero, increase count by 1
        //break the loop when we finish counting the number of leading zeros
        int i = 0;
        while (work[i] == '0') {
            //increase count if the character is zeros
            count += 1;
            i += 1;
        }
        //return the number of leading zeros
        return count;
    }

    //repair the entire chain to hide the data change
    public void repairChain() {
        //loop through each block until before the last block
        for (int i = 0; i < blocks.size()-1; i++) {
            String check = blocks.get(i).calculateHash();
            //if a hash is invalid
            if (getZeros(check) < blocks.get(i).getDifficulty() || !check.equals(blocks.get(i+1).getPreviousHash())) {
                //recalculate proof of work
                String newHash = blocks.get(i).proofOfWork();
                //update the previous hash field of the next block
                blocks.get(i+1).setPreviousHash(newHash);
            }
        }
        //set recentHash to be the new proof of work of the latest block
        recentHash = getLatestBlock().proofOfWork();
    }
}

