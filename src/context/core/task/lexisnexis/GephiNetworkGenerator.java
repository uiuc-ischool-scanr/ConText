/*
 * To change this template, choose Tools | Templates
 
 */
package context.core.task.lexisnexis;

import context.core.util.MyPair;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 *
 * @author Aale
 */
public class GephiNetworkGenerator {

    private Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph = new HashMap<CodebookEntity, List<MyPair<CodebookEntity, Integer>>>();
    String gexfOutputFile;

    /**
     *
     * @param graph
     * @param gexfOutputFile
     */
    public GephiNetworkGenerator(Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph, String gexfOutputFile) {
        this.graph = graph;
        this.gexfOutputFile = gexfOutputFile;
    }

    /**
     *
     */
    public void script() {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get a graph model - it exists because we have a workspace
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        final UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();
        //Create three nodes
        for (CodebookEntity node : graph.keySet()) {
            Node n0 = graphModel.factory().newNode(node.getName());
            n0.getNodeData().setLabel(node.getName().replace('_', ' '));
            n0.getAttributes().setValue("Type", node.getType());
            n0.getAttributes().setValue("Weight", 0);
            undirectedGraph.addNode(n0);
        }
        for (int i = 0; i < undirectedGraph.getNodeCount(); i++) {
            //System.out.println("node" + i + ": " + directedGraph.getNode(i));
        }
        for (CodebookEntity node : graph.keySet()) {
            Node s1 = undirectedGraph.getNode(node.getName());

            for (MyPair<CodebookEntity, Integer> other : graph.get(node)) {
                Node s2 = undirectedGraph.getNode(other.getFirst().getName());
                float weight = other.getSecond();
                addWeight(s1, weight);
                //System.out.println("");
                //System.out.println("s1=" + s1 + " s2=" + s2 + " weight=" + weight);
                Edge e0 = graphModel.factory().newEdge(s1, s2, weight, false);
                s1.getNodeData().setSize(s1.getNodeData().getSize() + weight);
                // s2.getNodeData().setSize(s2.getNodeData().getSize() + weight);
                s1.getNodeData().getTextData().setSize(s1.getNodeData().getTextData().getSize() + weight);
                // s2.getNodeData().getTextData().setSize(s2.getNodeData().getTextData().getSize() + weight);
                undirectedGraph.addEdge(e0);
            }
        }

//        for (CodebookEntity node : graph.keySet()) {
//            Node n0 = undirectedGraph.getNode(node.getName());
//            n0.getAttributes().setValue("Size", n0.getNodeData().getSize());
//        }
        float minsize = Integer.MAX_VALUE;
        float maxsize = Integer.MIN_VALUE;
        float desiredMinSize = 5;
        float desiredMaxSize = 50;
        for (Node n : undirectedGraph.getNodes()) {
            final float size = n.getNodeData().getSize();
            if (size < minsize) {
                minsize = size;
            }
            if (size > maxsize) {
                maxsize = size;
            }
        }

        float scale = (desiredMaxSize - desiredMinSize) / (maxsize - minsize);
        for (Node n : undirectedGraph.getNodes()) {
            float size = n.getNodeData().getSize();
            size = (size - minsize) * scale + desiredMinSize;
            n.getNodeData().setSize(size);
            n.getNodeData().getTextData().setSize(size);
        }

//        //Preview configuration
//        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
//        PreviewModel previewModel = previewController.getModel();
//        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
//        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.WHITE));
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.TRUE);
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 3);
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f);
//        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.BLACK);
//        previewController.refreshPreview();
//
//        //New Processing target, get the PApplet
//        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
//        PApplet applet = target.getApplet();
//        applet.init();
//
//        //Refresh the preview and reset the zoom
//        previewController.render(target);
//        target.refresh();
//        target.resetZoom();
//
//        //Add the applet to a JFrame and display
//        JFrame frame = new JFrame("Test Preview");
//        frame.setLayout(new BorderLayout());
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(applet, BorderLayout.CENTER);
//
//        frame.pack();
//        frame.setVisible(true);
        System.out.println("Nodes#: " + graphModel.getUndirectedGraph().getNodeCount());
        System.out.println("Directed Edges#: " + graphModel.getDirectedGraph().getEdgeCount());
        System.out.println("Undirected Edges#: " + graphModel.getUndirectedGraph().getEdgeCount());

        //Export full graph
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new File(gexfOutputFile));
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        //Export to Writer
//        Exporter exporterGraphML = ec.getExporter("graphml");     //Get GraphML exporter
//        exporterGraphML.setWorkspace(workspace);
//        StringWriter stringWriter = new StringWriter();
//        ec.exportWriter(stringWriter, (CharacterExporter) exporterGraphML);
        //System.out.println(stringWriter.toString());   //Uncomment this line

        //PDF Exporter config and export to Byte array
//        PDFExporter pdfExporter = (PDFExporter) ec.getExporter("pdf");
//        pdfExporter.setPageSize(PageSize.A0);
//        pdfExporter.setWorkspace(workspace);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ec.exportStream(baos, pdfExporter);
//        byte[] pdf = baos.toByteArray();
    }

    private void addWeight(Node s1, float weight) {
        Integer v1 = (Integer) s1.getAttributes().getValue("Weight");
        s1.getAttributes().setValue("Weight", v1 + weight);
    }
}
