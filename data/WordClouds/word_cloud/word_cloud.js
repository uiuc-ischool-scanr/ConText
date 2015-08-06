var topics = new Array([]);
var radius = 150;
var dims = {x: 200, y: 200} ;
d3.select("#txtWidth").property("value", width);
d3.select("#txtHeight").property("value", height);
d3.select("#txtSpacing").property("value", radius);
console.log(wordList);

var color = d3.scale.linear()
    .domain([0, item_count-1])
    .range(["yellow", "white"]);

for(i=0;i<item_count;i++){
  topics[i] = [];
}
topicFreqMax = 0;
topicFreqMin = wordList[0].frequency;
for(word in wordList){
	topics[wordList[word].topic].push(wordList[word]);
	if(wordList[word].frequency > topicFreqMax){
		topicFreqMax = wordList[word].frequency;
	}
	if(wordList[word].frequency < topicFreqMin){
		topicFreqMin = wordList[word].frequency;
	}
}

var fontScale = d3.scale.linear()
    .domain([topicFreqMin,topicFreqMax])
    .range([fontSize, 3*fontSize]);

console.log(topics);
var index = 1;

var fill = d3.scale.category10();

var nodes = d3.range(item_count).map(function(i) {
  return {index: i};
});


function drawForceCloud(words, index) {
    var boundingBox = d3.select("#word_clusters > svg > g#cluster_"+index).append("g")
        .attr("width", dims.x)
        .attr("height", dims.y);
    boundingBox.append("rect")
        .attr("transform", "translate(0,0)")
        .attr("x", 0)
        .attr("y", 0)
        .attr("width", dims.x)
        .attr("height", dims.y)
        .attr("stroke", "black")
    boundingBox.append("g")
        .attr("transform", "translate(0,0)")
      .selectAll("text")
        .data(words)
      .enter().append("text")
        .style("font-size", function(d) { return d.size + "px"; })
        .style("font-family", "Impact")
        .style("fill", function(d, i) { s = d.sentiment; return s == 1 ? "green" : s == 0 ? "blue": s==2?"black":"red"; })
        .attr("text-anchor", "middle")
        .attr("transform", function(d) {
          return "translate(" + [d.x+dims.x/2, d.y+dims.y/2] + ")rotate(" + d.rotate + ")";
        })
        .text(function(d) { return d.text; });
  }

function getSize(freq,i){
	//normalFreq = (freq - topicFreqMin)/(topicFreqMax-topicFreqMin);
	//return Math.log(1+normalFreq)*2 + fontSize;
  //return normalFreq*50 + fontSize;
  return fontScale(freq);
}

function drawClusters(){
      var force = d3.layout.force()
        .nodes(nodes)
        .size([width, height])
        .on("tick", tick)
        .start();

    var svg = d3.select("#word_clusters").append("svg")
        .attr("width", width)
        .attr("height", height)
        .style("display", "block")
        .style("margin", "auto");

    var node = svg.selectAll(".node")
        .data(nodes)
      .enter().append("g")
        .attr("class", "node")
        .attr("transform", function(d){return "translate("+(-dims.x/2)+","+(-dims.y/2)+")";})
      .attr("id", function(d){return "cluster_"+d.index;})
        .style("fill", function(d, i) { return color(i); })
        //.style("fill", "none")
        //.style("stroke", function(d, i) { return d3.rgb(fill(i & 3)).darker(2); })
        .call(force.drag)
        .on("mousedown", function() { d3.event.stopPropagation(); });

    svg.style("opacity", 1e-6)
      .transition()
        .duration(1000)
        .style("opacity", 1);

    d3.select("body > #word_clusters")
        .on("mousedown", mousedown);


    function tick(e) {

      // Push different nodes in different directions for clustering.
      var k = radius*0.1*e.alpha;
      nodes.forEach(function(o, i) {
        if(i==0){
          o.y += 0;
          o.x += 0;
        } else{
          var dispFactor = 1 + ((i-1)/4);
          var mov_val = k*dispFactor;
          o.y += (gen_i(i) < 3 ? mov_val : -mov_val);
          o.x += (i & 2 ? mov_val : -mov_val);

        }
      });

      node.attr("transform", function(d){
        return "translate("+(d.x-dims.x/2.0)+","+(d.y-dims.y/2.0)+")";
      });
    }

    function mousedown() {
      nodes.forEach(function(o, i) {
        o.x += (Math.random() - .5) * radius*5;
        o.y += (Math.random() - .5) * radius*5;
      });
      force.resume();
    }

      
	index = 0;
	for(;index<item_count;index++) {
		  d3.layout.cloud().size([dims.x, dims.y])
		    .words(topics[index].map(function(d) {
         //console.log(index);
			   return {text: d.text, size: getSize(d.frequency,index), sentiment: d.sentiment};
		  }))
		  .padding(2)
		  .rotate(function() { return 0; })
		  .font("Impact")
		  .fontSize(function(d) { return d.size; })
		  .on("end", function(words){
        return drawForceCloud(words, index);
      })
		  .start();
		}

	//d3.select("svg").attr("width", "100%");
}

function clearClusters(){
	d3.selectAll("svg").remove();
}

function updateCluster(){
	width = d3.select("#txtWidth").property("value");
	height = d3.select("#txtHeight").property("value");
	radius = d3.select("#txtSpacing").property("value");
	clearClusters();
	drawClusters();
	
}
drawClusters();

function gen_i(n){
  if (n === 0){
    return 0;
  }
  if ( (n%4) === 0){
    return 4;
  }
  return n%4;
}