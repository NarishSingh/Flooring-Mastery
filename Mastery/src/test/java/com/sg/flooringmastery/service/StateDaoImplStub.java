package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.StateDao;
import com.sg.flooringmastery.dao.StateReadException;
import com.sg.flooringmastery.model.State;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StateDaoImplStub implements StateDao {

    public TreeMap<String, State> onlyState = new TreeMap<>();

    public StateDaoImplStub() {
        onlyState.put("TX", new State("TX", new BigDecimal("4.45")));
    }

    public StateDaoImplStub(TreeMap<String, State> onlyState) {
        this.onlyState = onlyState;
    }

    @Override
    public List<State> getValidStates() throws StateReadException {
        if (onlyState.isEmpty()) {
            throw new StateReadException("No valid states");
        } else {
            return new ArrayList<>(onlyState.values());
        }

    }

    @Override
    public State readStateByID(String stateAsText) throws InvalidStateException {
        if (onlyState.get(stateAsText) == null) {
            throw new InvalidStateException("Invalid state");
        } else {
            return onlyState.get(stateAsText);
        }
    }
}
