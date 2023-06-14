package Player.AlphaZeroDotFive;

import Player.AlphaZeroDotFive.MonteCarloTree.Move;
import Player.AlphaZeroDotFive.NeuralNets.Interfaces.IPolicyNetwork;
import Player.AlphaZeroDotFive.NeuralNets.Interfaces.IValueNetwork;
import Player.Player;

//TODO
public class AlphaZeroDotFiveAgent implements Player {
    public IPolicyNetwork policyNetwork;
    public IValueNetwork valueNetwork;
    @Override
    public Move[] getValidMoves() {
        return new Move[0];
    }
}
