package com.iit;
import com.iit.LeafNode;
import com.iit.SuperPeer;

public class Main {

    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Aruments missing");
        }
        else if(args[0].equals("leaf")){
            LeafNode.main(new String[]{args[1]});
        }else if(args[0].equals("superpeer")){
            SuperPeer.main(new String[]{});
        }else{
            System.out.println("Invalid aruments to start");
        }
    }
}
