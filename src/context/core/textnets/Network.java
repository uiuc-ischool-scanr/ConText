/**
 *
 */
package context.core.textnets;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.datalab.api.datatables.AttributeTableCSVExporter;
import org.gephi.graph.api.Attributable;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 * @author Shubhanshu
 *
 */
public class Network {

    private ProjectController pc;
    private Workspace workspace;
    private GraphModel graphModel;

    /**
     *
     */
    public UndirectedGraph graph;

    /**
     *
     */
    public static enum FileType {

        /**
         *
         */
        CSV, 

        /**
         *
         */
        TSV, 

        /**
         *
         */
        GRAPHML
    };
    private String edgeTableDelim = ",";
    
    //Add edgeID, initialize edgeID =1 when network generated
	//thus each session will has the default edgeID starts from 1
    private int edgeID;

    /**
     * @return the edgeTableDelim
     */
    public synchronized String getEdgeTableDelim() {
        return edgeTableDelim;
    }

    /**
     * @param edgeTableDelim the edgeTableDelim to set
     */
    public synchronized void setEdgeTableDelim(String edgeTableDelim) {
        this.edgeTableDelim = edgeTableDelim;
    }

    public synchronized int getEdgeID(){
    	return edgeID;
    }
    
    public synchronized void setEdgeID(int edgeID){
    	this.edgeID=edgeID;
    }
    
    private String edgeTablePath = "";

    /**
     * @return the edgeTablePath
     */
    public synchronized String getEdgeTablePath() {
        return edgeTablePath;
    }

    /**
     * @param edgeTablePath the edgeTablePath to set
     */
    public synchronized void setEdgeTablePath(String edgeTablePath) {
        this.edgeTablePath = edgeTablePath;
    }

    /**
     *
     */
    public Network() {
        // TODO Auto-generated constructor stub
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        workspace = pc.getCurrentWorkspace();
        graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
       
        graph = graphModel.getUndirectedGraph();
        
        
        this.setEdgeTableDelim(",");
        this.setEdgeID(1);
    }

    /**
     *
     * @param id
     * @return
     */
    public Node genNode(String id) {
        id = id.replaceAll(this.edgeTableDelim, "");
        Node n = graphModel.factory().newNode(id);
        n.getAttributes().setValue("LABEL", "");
        n.getAttributes().setValue("FREQUENCY", 0);
        Node temp = graph.getNode(id);
        if (temp != null) {
            n = temp;
            Integer f = (Integer) n.getAttributes().getValue("FREQUENCY");
            f += 1;
            n.getAttributes().setValue("FREQUENCY", f);
//			System.out.println("Existing Node: "+n.getId()+", "+n.toString());
        }
        return n;
    }

    /**
     *
     * @param n1
     * @param n2
     * @return
     */
    public Edge getEdge(Node n1, Node n2) {
        Edge ed = graph.getEdge(n1, n2);
        float w = 1;
        if (ed != null) {
            w = ed.getWeight();
            ed.setWeight(w + 1);
            return ed;
        }else{
        	 //Add edgeID, initialize edgeID =1 when network generated
        	 //thus each session will has the default edgeID starts from 1
        	 ed = graphModel.factory().newEdge(String.valueOf(edgeID),n1, n2, 1, false);
             //ed = graphModel.factory().newEdge(n1, n2);
             //ed.setWeight(w);		
        	 edgeID++;
             return ed;
        }
       
    }

    /**
     *
     * @param source
     * @param target
     */
    public void addEdge(WordNode source, WordNode target) {
        Node n1 = genNode(source.text);
        Node n2 = genNode(target.text);
        n1.getAttributes().setValue("LABEL", source.label);
        n2.getAttributes().setValue("LABEL", target.label);
        this.graph.addNode(n1);
        this.graph.addNode(n2);
        //System.err.println("Edge: "+word+","+target);

        Edge ed = getEdge(n1, n2);
        ed.getAttributes().setValue("SOURCE_LBL", source.label);
        ed.getAttributes().setValue("TARGET_LBL", target.label);
        this.graph.addEdge(ed);

    }

    /**
     *
     * @param fileName
     * @param outputDir
     * @param nt
     */
    public void saveNet(String fileName, String outputDir, FileType nt) {
        switch (nt) {
            case CSV:
                fileName += ".csv";
                break;
            case GRAPHML:
                fileName += ".graphml";
                break;
            default:
                System.err.println("Invalid filetype for saving graph. Use either CSV or graphml");
                break;
        }
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            //ec.exportFile(new File("headless_simple.graphml"));
            ec.exportFile(new File(outputDir + "/" + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
        AttributeModel am = ac.getModel();
        AttributeTable at = am.getEdgeTable();
        ArrayList<Integer> colIds = new ArrayList<Integer>();
        colIds.add(AttributeTableCSVExporter.FAKE_COLUMN_EDGE_SOURCE);
        colIds.add(AttributeTableCSVExporter.FAKE_COLUMN_EDGE_TARGET);
        colIds.add(AttributeTableCSVExporter.FAKE_COLUMN_EDGE_TYPE);
        for (int i = 0; i < at.getColumns().length; i++) {
            colIds.add(i);
        }
        Attributable[] att = graph.getEdges().toArray();

        System.err.println("Going to print Edges Table");
        try {
            AttributeTableCSVExporter.writeCSVFile(at, new File(this.edgeTablePath), ',',
                    Charset.defaultCharset(), colIds.toArray(new Integer[at.getColumns().length + 3]), att);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
