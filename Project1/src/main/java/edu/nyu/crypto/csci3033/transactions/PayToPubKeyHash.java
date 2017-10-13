package edu.nyu.crypto.csci3033.transactions;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.utils.ExponentialBackoff.Params;

import java.io.File;
import java.security.interfaces.ECKey;

import static org.bitcoinj.script.ScriptOpCodes.*;
import static org.bitcoinj.script.ScriptOpCodes.OP_VERIFY;

/**
 * Created by bbuenz on 24.09.15.
 */
public class PayToPubKeyHash extends ScriptTransaction {
    // TODO: Problem 1
	private org.bitcoinj.core.ECKey key;

    public PayToPubKeyHash(NetworkParameters parameters, File file, String password) throws AddressFormatException {
        super(parameters, file, password);
        String vanityPrivateKey = "5J3vXukpRFf2gTN3YN1HfTd7orTFsjAeaE5KccyQwYXDds9PM2c";
        DumpedPrivateKey privKey = new DumpedPrivateKey(parameters, vanityPrivateKey);
        key = privKey.getKey(); //this is an ECKey
        
    }

    @Override
    //what the user sends
    public Script createInputScript() {
        // TODO: Create a P2PKH script
        // TODO: be sure to test this script on the mainnet using a vanity address
    		ScriptBuilder builder = new ScriptBuilder();
    		builder.op(OP_DUP);
    		builder.op(OP_HASH160);
    		builder.data(key.getPubKeyHash()); 
    		builder.op(OP_EQUALVERIFY);
    		builder.op(OP_CHECKSIG);
    		return builder.build();

    }

    @Override
    //what the receiver provides
    public Script createRedemptionScript(Transaction unsignedTransaction) {
        // TODO: Redeem the P2PKH transaction
        TransactionSignature txSig = sign(unsignedTransaction, key);
        ScriptBuilder builder = new ScriptBuilder();
        builder.data(txSig.encodeToBitcoin());
        builder.data(key.getPubKey());
        return builder.build();
    }
}
