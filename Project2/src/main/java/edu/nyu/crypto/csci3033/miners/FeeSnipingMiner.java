//double expectedProfit = Math.pow((this.getHashRate()/this.stats.getTotalHashRate()), 2)*(block.getBlockValue() + expectedValue);
//        		
//        		
//if(expectedProfit > (this.getHashRate()/this.stats.getTotalHashRate())*expectedValue) 
//this.numOfBlocks += 1;
//    		
//    		this.totalValue += block.getBlockValue();
//    		 
//    		double expectedValue = totalValue/numOfBlocks;
package edu.nyu.crypto.csci3033.miners;

import edu.nyu.crypto.csci3033.blockchain.Block;
import edu.nyu.crypto.csci3033.blockchain.NetworkStatistics;


public class FeeSnipingMiner extends BaseMiner implements Miner {

	private Block currentHead;
	private Block secondHead;
	private boolean attacking = false;
	private double hashPower;
	private double totalValue = 0;
	private double totalBlocks = 0;
	
	
    public FeeSnipingMiner(String id, int hashRate, int connectivity) {
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
    	
    	this.totalBlocks++;
    	this.totalValue += block.getBlockValue();
    	
    	double nextBlockValue = this.totalValue/this.totalBlocks;
    	
    	//first round wont attack
    	if(attacking) {
    		
    		if(isMinerMe) {
                if (block.getHeight() > currentHead.getHeight()) {
                    this.currentHead = block;
                }
            }
            else{
                if (currentHead == null) {
                    currentHead = block;
                } else if (block != null && block.getHeight() > currentHead.getHeight()) {
                    this.currentHead = block;

                }
            }
    	}

    	//attacking now
    else {
    	
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
          
            if(!block.getMinedBy().equals("Attacker") && block.getHeight() > secondHead.getHeight()+1) {
            		this.attacking = false;
            }
            
        	//calculate whether you should attack
        	double expectedProfit = Math.pow(this.hashPower, 2)*(block.getBlockValue() + nextBlockValue);
        
        	if(expectedProfit > (block.getBlockValue() + nextBlockValue)) {
        		this.attacking = true;
        	}
            
        }
        

       
    }
    	

    	
    	

    	
    }

    
	@Override
	public void networkUpdate(NetworkStatistics statistics) {
		this.hashPower = (double)(this.getHashRate()/statistics.getTotalConnectivity());
	
		
	}

	@Override
	public void initialize(Block genesis, NetworkStatistics statistics) {
		this.currentHead = genesis;
		
	}

}