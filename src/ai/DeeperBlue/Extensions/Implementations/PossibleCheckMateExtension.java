package ai.DeeperBlue.Extensions.Implementations;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Extensions.Extension;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueExtensionNode;

//This extension runs a search of maxDepth 2 to and looks if there are checkmates
public class PossibleCheckMateExtension extends Extension {
    public PossibleCheckMateExtension(){
        this.id = POSSIBLE_CHECKMATE;
    }
    @Override
    public void expand(DeeperBlueExtensionNode leafNode) throws DeeperBlueException {
        expandChildren(leafNode);
    }

    @Override
    public int interest(DeeperBlueExtensionNode leafNode) {
        if(!leafNode.checkMated){
            int result = 100 - leafNode.maybeValidMovesPlayer.size() * 10;
            if(result > 50){
                leafNode.interesting = true;
            }
            if(result > leafNode.currentHighestInterestValue){
                leafNode.currentHighestInterestValue = result;
                leafNode.currentHighestExtensionId = POSSIBLE_CHECKMATE;
            }
            return result;
        }else{
            return 0;
        }

    }

}
