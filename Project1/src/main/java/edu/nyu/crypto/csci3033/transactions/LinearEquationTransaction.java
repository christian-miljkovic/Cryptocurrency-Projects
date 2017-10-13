package edu.nyu.crypto.csci3033.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.io.File;
import java.math.BigInteger;
import java.net.UnknownHostException;

import static org.bitcoinj.script.ScriptOpCodes.*;

/**
 * Created by bbuenz on 24.09.15.
 */
public class LinearEquationTransaction extends ScriptTransaction {
    // TODO: Problem 2
	byte[] rightSide; //2861
	byte[] leftSide; //1951
	
	
	
    public LinearEquationTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
        rightSide = encode(BigInteger.valueOf(2861));
        leftSide = encode(BigInteger.valueOf(1951));
        
        
    }

    @Override
    public Script createInputScript() {
        // TODO: Create a script that can be spend by two numbers x and y such that x+y=first 4 digits of your suid and |x-y|=last 4 digits of your suid (perhaps +1)
    		ScriptBuilder builder = new ScriptBuilder();
    		builder.op(OP_OVER);
    		builder.op(OP_OVER);
    		builder.op(OP_ADD);
    		builder.data(leftSide);
    		builder.op(OP_NUMEQUALVERIFY);
    		builder.op(OP_SUB);
    		builder.data(rightSide);
    		builder.op(OP_NUMEQUAL);
   
        return builder.build();
    }

    //N19512861
    @Override
    public Script createRedemptionScript(Transaction unsignedScript) {
        // TODO: Create a spending script
    		ScriptBuilder builder = new ScriptBuilder();
    		BigInteger x = BigInteger.valueOf(2406);
    		BigInteger y = BigInteger.valueOf(-455);
    		builder.data(encode(x));
    		builder.data(encode(y));
        return builder.build();
    }

    private byte[] encode(BigInteger bigInteger) {
        return Utils.reverseBytes(Utils.encodeMPI(bigInteger, false));
    }
}

