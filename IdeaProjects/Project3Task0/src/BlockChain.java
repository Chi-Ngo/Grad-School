import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This program displays a menu to the user
 * The user can choose the operation they want with the blockchain
 * The time taken to compute proof of work increases with difficulty
 * While it takes approximately the same time for difficulty 2,3 and 4
 * It takes longer for difficulty 5, and exponentially longer for difficulty 6 and above
 */

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
                    //get the string of required leading zeros based on difficulty
                    String zeros = "0";
                    while (zeros.length() < blocks.get(i).getDifficulty()) {
                        zeros += "0";
                    }
                    //print out the notification
                    System.out.println("..Improper hash on node " + i + ". Does not begin with " + zeros);
                    //chain is not valid
                    return false;
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
                    //print out the notification
                    System.out.println("..Improper hash on node " + i + ". Does not begin with " + zeros);
                    //chain is not valid
                    return false;
                }
            }
        }
        //if all blocks are valid, the entire chain is valid, return true
        return true;
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


    //main method of the program
    //the average time to add a block of difficulty 4 is 120 milliseconds
    //the average time to add a block of difficulty 5 is 500 milliseconds
    //the average time to verify a chain with blocks of difficulty 4 is 0 milliseconds
    //the average time to verify a chain with blocks of difficulty 5 is 3 milliseconds
    //the average time to repair a chain with blocks of difficulty 4 is 420 milliseconds
    //the average time to repair a chain of blocks of difficulty 5 is 1000 milliseconds
    public static void main(String[] args) {
        //set up a new blockchain using the constructor
        BlockChain bc = new BlockChain();
        //add in a genesis before program begins interacting with the client
        Block genesis = new Block(0, bc.getTime(), "Genesis", 2);
        bc.addBlock(genesis);
        //typed to take in user input
        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        String input;
        try {
            //keep doing this loop as long as user doesn't choose 6 (exit)
            do {
                //display the menu with choices
                System.out.println("0. View basic blockchain status.\n" +
                        "1. Add a transaction to the blockchain.\n" +
                        "2. Verify the blockchain.\n" +
                        "3. View the blockchain.\n" +
                        "4. Corrupt the chain.\n" +
                        "5. Hide the corruption by repairing the chain.\n" +
                        "6. Exit.");
                input = typed.readLine();
                //if user chooses option 0
                if (input.equals("0")) {
                    //display the basic information of the blockchain
                    System.out.println("Current size of chain: " + bc.getChainSize());
                    System.out.println("Current hashes per second by this machine: " + bc.hashesPerSecond());
                    System.out.println("Difficulty of most recent block: " + bc.getLatestBlock().getDifficulty());
                    System.out.println("Nonce for most recent block: " + bc.getLatestBlock().getNonce());
                    System.out.println("Chain hash: " + bc.recentHash);
                }
                // else if user chooses option 1
                else if (input.equals("1")) {
                    //prompt for difficulty of the new block
                    System.out.println("Enter difficulty > 0");
                    int difficulty = Integer.parseInt(typed.readLine());
                    //prompt for the transaction
                    System.out.println("Enter transaction");
                    String data = typed.readLine();
                    //get the index of the latest block on the chain
                    //add 1 to that index to find index of the next block
                    int index = bc.getLatestBlock().getIndex() + 1;
                    //get the current system time
                    Timestamp begin = bc.getTime();
                    //create a new block using the block constructor
                    Block newBlock = new Block(index, begin, data, difficulty);
                    //add the block to the chain
                    bc.addBlock(newBlock);
                    //find the end time after the program adds the new block successfully
                    Timestamp end = bc.getTime();
                    //find the time taken for the program to add the new block
                    long difference = end.getTime() - begin.getTime();
                    //display the information to the user
                    System.out.println("Total execution time to add this block was " + difference + " milliseconds");
                }
                //else if user chooses option 2
                else if (input.equals("2")) {
                    //get the current time before the verification process begins
                    Timestamp begin = bc.getTime();
                    System.out.println("Verifying entire chain");
                    //call isChainValid() to verify the chain
                    System.out.println("Chain verification: " + bc.isChainValid());
                    //get the end time after the process is done
                    Timestamp end = bc.getTime();
                    //find the time taken to verify the entire chain
                    long difference = end.getTime() - begin.getTime();
                    //display the information to the user
                    System.out.println("Total execution time required to verify the chain was " + difference + " milliseconds");
                }
                //else if user chooses option 3
                else if (input.equals("3")) {
                    //print the chain in JSON format
                    System.out.println(bc.toString());
                }
                //else if user chooses option 4
                else if (input.equals("4")) {
                    System.out.println("Corrupt the Blockchain");
                    //prompt the user for the id of the block to corrupt
                    System.out.print("Enter block ID of block to Corrupt ");
                    int id = Integer.parseInt(typed.readLine());
                    //get the new data to insert in the block
                    System.out.println("Enter new data for block " + id);
                    String data = typed.readLine();
                    //set the new data for the specified block
                    bc.blocks.get(id).setData(data);
                    //inform the user when the process is done
                    System.out.println("Block " + id + " now holds " + data);
                }
                //else if user chooses 5
                else if (input.equals("5")) {
                    //get the current before the reparation process begins
                    Timestamp begin = bc.getTime();
                    System.out.println("Repairing the entire chain");
                    //repair the chain
                    bc.repairChain();
                    //get the end time after the process is finished
                    Timestamp end = bc.getTime();
                    //find the time taken to fix the entire chain
                    long difference = end.getTime() - begin.getTime();
                    //display the information to the user
                    System.out.println("Total execution time required to repair the chain was " + difference + " milliseconds");
                }
                //if user chooses 6, while loop exits, program ends
            } while (!input.equals("6"));
            //catch blocks for exceptions
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
