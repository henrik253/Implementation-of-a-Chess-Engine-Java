package ai.DeeperBlue.Workers;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueExtensionNode;

public class Worker extends Thread{
    private final Workerpool pool;
    boolean inInterestMode;
    public Worker(Workerpool pool){
        this.pool = pool;
        this.inInterestMode = true;
    }
    public void run(){
        if(inInterestMode){
            runInInterestMode();
        }else{
            try {
                runInExpansionMode();
            } catch (DeeperBlueException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void runInExpansionMode() throws DeeperBlueException {
        DeeperBlueExtensionNode currentLeaf = pool.getNewLeaf();
        while(currentLeaf != null){
            expandAndBackProp(currentLeaf);
            currentLeaf = pool.getNewLeaf();
        }
    }

    private void expandAndBackProp(DeeperBlueExtensionNode currentLeaf) throws DeeperBlueException {
        int mostInterestingIndex = getMostInterestingIndex(currentLeaf);
        this.pool.agent.extensions[mostInterestingIndex].expand(currentLeaf);
        this.pool.agent.backPropagate(currentLeaf);
    }

    private static int getMostInterestingIndex(DeeperBlueExtensionNode currentLeaf) {
        int mostInterestingIndex = 0;
        int mostInterestingValue = -1;
        for (int i = 0; i < currentLeaf.extensionInterestValues.length; i++) {
            if(mostInterestingValue < currentLeaf.extensionInterestValues[i]){
                mostInterestingValue = currentLeaf.extensionInterestValues[i];
                mostInterestingIndex = i;
            }
        }
        return mostInterestingIndex;
    }

    private void runInInterestMode() {
        DeeperBlueExtensionNode currentLeaf = pool.getNewLeaf();
        while(currentLeaf != null){
            for (int extensionIndex = 0; extensionIndex < this.pool.agent.extensions.length; extensionIndex++) {
                currentLeaf.extensionInterestValues[extensionIndex] = this.pool.agent.extensions[extensionIndex].interest(currentLeaf);
            }
            currentLeaf = pool.getNewLeaf();
        }
    }
    public void changeToExpansionMode(){
        this.inInterestMode = false;
    }

    public void changeToInterestMode() {
        this.inInterestMode = true;
    }
}
