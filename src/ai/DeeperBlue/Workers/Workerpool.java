package ai.DeeperBlue.Workers;

import ai.DeeperBlue.DeeperBlueAgent;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueExtensionNode;

import java.util.ArrayList;

public class Workerpool {
    DeeperBlueAgent agent;
    Worker[] workers;
    private ArrayList<DeeperBlueExtensionNode> leaves;
    private boolean[] alreadyChosen;

    public Workerpool(int numOfExtensionWorkers, DeeperBlueAgent agent) {
        initWorkers(numOfExtensionWorkers);
        this.agent = agent;
    }

    private void initWorkers(int numOfExtensionWorkers) {
        this.workers = new Worker[numOfExtensionWorkers];
        for (int i = 0; i < this.workers.length; i++) {
            this.workers[i] = new Worker(this);
        }
    }

    public void run(ArrayList<DeeperBlueExtensionNode> input) throws InterruptedException {
        long start = System.currentTimeMillis();
        leaves = getBestLeaves(input);
        alreadyChosen = new boolean[leaves.size()];
        runWorkers();
        leaves = getInterestingLeaves(input);
        alreadyChosen = new boolean[leaves.size()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker(this);
            workers[i].changeToExpansionMode();
        }
        runWorkers();
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker(this);
            workers[i].changeToInterestMode();
        }
        long end = System.currentTimeMillis();
        System.out.println("ExtensionTime: " + (end - start));
    }

    private ArrayList<DeeperBlueExtensionNode> getBestLeaves(ArrayList<DeeperBlueExtensionNode> input) {
        ArrayList<DeeperBlueExtensionNode> result = new ArrayList<>();
        for (int i = 0; i < agent.MAX_EXTENSIONS_MULTI_THREADED; i++) {
            result.add(input.get(i));
        }
        return result;
    }

    private static ArrayList<DeeperBlueExtensionNode> getInterestingLeaves(ArrayList<DeeperBlueExtensionNode> input) {
        ArrayList<DeeperBlueExtensionNode> result = new ArrayList<>();
        for(DeeperBlueExtensionNode node : input){
            if(node.interesting){
                result.add(node);
            }
        }
        return result;
    }

    private void runWorkers() throws InterruptedException {
        for(Worker worker : this.workers){
            worker.start();
        }
        for(Worker worker : this.workers){
            worker.join();
        }
    }

    public synchronized DeeperBlueExtensionNode getNewLeaf() {
        for(int i = 0; i < alreadyChosen.length; i++){
            if(!alreadyChosen[i]){
                alreadyChosen[i] = true;
                return leaves.get(i);
            }
        }
        return null;
    }

    public void setLeaves(ArrayList<DeeperBlueExtensionNode> leaves){
        this.leaves = leaves;
    }
}
