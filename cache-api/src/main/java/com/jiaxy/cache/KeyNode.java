package com.jiaxy.cache;

import java.io.Serializable;
import java.util.*;

/**
 * @author: wutao
 * @version: $Id:KeyNode.java 2014/01/10 10:51 $
 *
 */
public class KeyNode implements Serializable{

    private KeyNode parent;

    private transient List<KeyNode> children = new ArrayList<KeyNode>();

    private String namespace;

    private String desc;

    private transient List<Key> keyList = new ArrayList<Key>();

    public KeyNode() {
    }



    public KeyNode getParent() {
        return parent;
    }

    public void setParent(KeyNode parent) {
        this.parent = parent;
    }

    public List<KeyNode> getChildren() {
        return children;
    }

    public void setChildren(List<KeyNode> children) {
        this.children = children;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Key> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<Key> keyList) {
        this.keyList = keyList;
    }

    public static void print(KeyNode keyNode){

        Queue<KeyNode> queue = new LinkedList<KeyNode>();
        int index = 0;
        int level = 6;
        int leaf = level;
        while ( keyNode != null || (!queue.isEmpty() )){
            if ( keyNode != null ){
                for ( int i = 0 ;i < level ;i++ ){
                    System.out.print("\t");
                }
                System.out.print(keyNode.getNamespace());
                if ( keyNode.getKeyList() != null ){
                    for (Key key : keyNode.getKeyList()){
                        System.out.print("--params--"+key.getParamsMap());
                    }
                }
                if ( keyNode.getChildren() != null){
                    queue.addAll(keyNode.getChildren());
                }else {

                }
            }else {
            }
            keyNode =queue.poll();

            System.out.println();
        }
    }



    public static int leafSize(Integer size,KeyNode keyNode){
        if ( keyNode.getChildren() == null || keyNode.getChildren().size() == 0 ){
            return ++size;
        }else {
            for (KeyNode c:keyNode.getChildren()){
               size = leafSize(size, c);
            }
            return size;
        }
    }

    public String toString(String lftStr, String append) {
        StringBuilder b = new StringBuilder();
        b.append(append + namespace);
        b.append("\n");
        if ( getKeyList() != null && getKeyList().size() > 0 ){
            for (Key key : getKeyList() ){
                b.append(lftStr).append(" ").append(".").append(" Key ").append(key).append("\n");
                b.append(lftStr).append(" ").append(".").append(" realkey ").append(key.getKey()).append("\n");
                b.append(lftStr).append("--------------------------------").append("\n");
            }
        }
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size() - 1; i++) {
                KeyNode child = children.get(i);

                b.append(lftStr+child.toString(lftStr + "│", "├"));

            }
            b.append(lftStr + children.get(children.size() - 1).toString(
                    lftStr + " ", "└"));

        }
        return b.toString();

    }
    public String toHTMLString(String lftStr, String append) {
        StringBuilder b = new StringBuilder();
        b.append(append + namespace);
        b.append("<br/>");
        if ( getKeyList() != null && getKeyList().size() > 0 ){
            for (Key key : getKeyList() ){
                b.append(lftStr).append("&nbsp;").append(".").append(" Key ").append(key).append("<br/>");
                b.append(lftStr).append("&nbsp;").append(".").append(" realkey ").append(key.getKey()).append("<br/>");
                b.append(lftStr).append("-------------------------------------------------------").append("<br/>");
            }
        }
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size() - 1; i++) {
                KeyNode child = children.get(i);

                b.append(lftStr+child.toHTMLString(lftStr + "│", "├"));

            }
            b.append(lftStr + children.get(children.size() - 1).toHTMLString(
                    lftStr + "&nbsp;", "└"));

        }
        return b.toString();

    }

    public void printTree(){
        System.out.println(toString("", ""));
    }


}
