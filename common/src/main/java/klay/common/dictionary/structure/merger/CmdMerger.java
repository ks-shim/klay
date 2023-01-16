package klay.common.dictionary.structure.merger;

public interface CmdMerger<T> {

    T merge(T preCmd, T curCmd);
}
