package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieTree {
    private TrieNode root;

    public TrieTree(List<String> sentences) {
        root = new TrieNode();

        for(String sent: sentences) {
            insert(sent);
        }
    }

    private class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isSent = false;
    }

    public void insert(String sent) {
        TrieNode curr = root;
        for(char c: sent.toCharArray()) {
            TrieNode next = curr.children.get(c);
            if(next==null) {
                next = new TrieNode();
                curr.children.put(c, next);
            }
            curr = next;
        }
        curr.isSent = true;
    }

    public List<String> query(String prefix) {
        TrieNode curr = root;
        List<String> result = new ArrayList<String>();
        for(char c: prefix.toCharArray()) {
            TrieNode next = curr.children.get(c);
            if(next==null) return result;
            curr = next;
        }

        collectSentWithPrefix(result, curr, prefix);

        return result;
    }

    private void collectSentWithPrefix(List<String> result, TrieNode startNode, String prefix) {
        if(startNode.isSent) result.add(prefix);

        for(char c: startNode.children.keySet()) {
            collectSentWithPrefix(result, startNode.children.get(c), prefix+c);
        }
    }



}
