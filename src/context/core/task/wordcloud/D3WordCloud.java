package context.core.task.wordcloud;

/**
 *
 * @author Aale
 */
public class D3WordCloud {

	String javaScript = "";
	/**
	 * @param args
     * @param percentWeight
	 */
	public D3WordCloud( int height, int width, String[] names, float[] percentWeight) {
		
		String javaScriptString = "<!DOCTYPE html><meta charset=\"utf-8\"><script src=\"d3/d3.v3.js\"></script><script src=\"d3/d3.layout.cloud.js\"></script><body><script>  var fill = d3.scale.category20(); function SizeFunc(index){ var Sizes = [";
		for(float weight : percentWeight){
			javaScriptString = javaScriptString + Float.toString(weight) + ',';
		}
		javaScriptString = javaScriptString.substring(0, javaScriptString.length()-1);
		
		javaScriptString = javaScriptString + "]; return Sizes[index];}  d3.layout.cloud().size([" + Integer.toString(width) + ',' + Integer.toString(height) + "]).words([";
		
		for(String name : names){
			javaScriptString = javaScriptString +"\""+ name + "\"" + ',';
		}
		javaScriptString = javaScriptString.substring(0, javaScriptString.length()-1);
		
		javaScriptString = javaScriptString + "].map(function(d,index) { return {text: d, size: 100*SizeFunc(index)};})).rotate(function() { return ~~(Math.random() * 2) * 90; }) .font(\"Impact\") .fontSize(function(d,index) { return d.size; }).on(\"end\", draw).start();  function draw(words) { d3.select(\"body\").append(\"svg\").attr(\"width\", "+ Integer.toString(width) + ").attr(\"height\"," + Integer.toString(height) + ").append(\"g\") .attr(\"transform\", \"translate(" + Integer.toString(width/2) + ',' + Integer.toString(height/2) + ")\").selectAll(\"text\").data(words).enter().append(\"text\").style(\"font-size\", function(d) { return d.size + \"px\"; }).style(\"font-family\", \"Impact\").style(\"fill\", function(d, i) { return fill(i); }) .attr(\"text-anchor\", \"middle\").attr(\"transform\", function(d) {return \"translate(\" + [d.x, d.y] + \")rotate(\" + d.rotate + \")\"; }).text(function(d) { return d.text; });  }</script>";
		javaScript = javaScriptString;
	}

    /**
     *
     * @return
     */
    public String getD3JavaScript(){
		return javaScript;
	}
}
