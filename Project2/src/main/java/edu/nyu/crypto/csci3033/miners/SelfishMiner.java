package edu.nyu.crypto.csci3033.miners;

import edu.nyu.crypto.csci3033.blockchain.Block;
import edu.nyu.crypto.csci3033.blockchain.NetworkStatistics;

public class SelfishMiner extends BaseMiner implements Miner {


	private Block currentHead;
	private Block doubleHead;
	private Block announceHead = currentHead;
	
	
	
    public SelfishMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);

    }
    
    @Override
    public Block currentlyMiningAt() {
    		
    		if(doubleHead != null) {
    			this.currentHead = doubleHead;
    			return doubleHead;
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
        		
        		if(block.getPreviousBlock().getMinedBy().equals("Attacker")) {
        			//System.out.println("There were two");
        			this.doubleHead = block;
        		}
        		else {
        			if(!block.getMinedBy().equals("Satoshi")) {
        				this.currentHead = block;
        			}
        		}
        		
        		
        	}
        else {
        		
        		this.announceHead = this.doubleHead;
        		
        	
        }
//        
//        System.out.println(currentHead);
//        System.out.println(doubleHead);
//        System.out.println(announceHead);
//        System.out.println("");

    }

    
	@Override
	public void networkUpdate(NetworkStatistics statistics) {
		
	
		
	}

	@Override
	public void initialize(Block genesis, NetworkStatistics statistics) {
		this.currentHead = genesis;
		
	}

}

