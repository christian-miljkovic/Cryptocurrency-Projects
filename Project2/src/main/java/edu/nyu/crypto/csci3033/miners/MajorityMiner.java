package edu.nyu.crypto.csci3033.miners;

import edu.nyu.crypto.csci3033.blockchain.Block;
import edu.nyu.crypto.csci3033.blockchain.NetworkStatistics;


public class MajorityMiner extends BaseMiner implements Miner {

	private Block currentHead;
	private Block secondHead;
	
	
    public MajorityMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);

    }
    
    @Override
    public Block currentlyMiningAt() {
    	
    		if(secondHead.getHeight() > currentHead.getHeight()) {
    			return secondHead;
    		}
    		else {
    			return currentHead;
    		}
    }

    @Override
    public Block currentHead() {
        return currentHead;
    }
    
    
    @Override
    public void blockMined(Block block, boolean isMinerMe) {
    	
        if(isMinerMe) {
        	this.currentHead = block;
        }
        else{
            if (currentHead == null) {
                currentHead = block;
            } else if (block != null && block.getHeight() > currentHead.getHeight()) {
            	
            		//this is because some block might get propagated before my block
            		//even though our chain is longer. Therefore to prevent the other miners
            		//from simply working off my hashing power
            		this.secondHead = currentHead;

            }
        }
       System.out.println(block); 
    }

    
	@Override
	public void networkUpdate(NetworkStatistics statistics) {
		
	
		
	}

	@Override
	public void initialize(Block genesis, NetworkStatistics statistics) {
		this.currentHead = genesis;
		
	}

}
