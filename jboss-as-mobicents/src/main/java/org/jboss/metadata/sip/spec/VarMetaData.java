package org.jboss.metadata.sip.spec;

/**
 * @author josemrecio@gmail.com
 *
 */
public class VarMetaData extends ConditionMetaData {
    private static final long serialVersionUID = 1;
    private String var;
    private boolean ignoreCase;

    /**
     * @param var the var to set
     */
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * @return the var
     */
    public String getVar() {
        return var;
    }

    /**
     * @param ignoreCase the ignoreCase to set
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    /**
     * @return the ignoreCase
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }
}
