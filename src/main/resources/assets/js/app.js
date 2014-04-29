var app = angular.module("myApp", []);

app.directive('map', function($http) {
	return {
		restrict: 'E',
		replace: true,
		template: '<div></div>',
		link: function(scope, element, attrs) {
			console.log(element);
			var myOptions = {
				zoom: 14,
				center: new google.maps.LatLng(37.442975, -122.1630475),
				mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			var map = new google.maps.Map(document.getElementById(attrs.id), myOptions);
			
			var infowindow = new google.maps.InfoWindow();
			
			fillMap = function() {
				$http({
					method: 'GET',
					dataType: 'json',
					url: '/bigdata/v1/users/1/competition?callback=JSON_CALLBACK',
					headers: {
						'Content-Type': 'application/json'
					}
				}).success(function(data, status, headers, config) {
					var y = data;
					for (var i = 0; i < data.length; i++ ){
						var z = y[i];
						var myLatlng = new google.maps.LatLng(z.latitude, z.longitude);
						if (z.business_id == "VFslQjSgrw4Mu5_Q1xk1KQ") {
							var marker = new google.maps.Marker({
								position: myLatlng,
								map: map,
								title: z.name + " (You!)",
								icon: "http://www.google.com/intl/en_us/mapfiles/ms/micons/blue-dot.png"
							});
						} else {
							var marker = new google.maps.Marker({
								position: myLatlng,
								map: map,
								title: z.name
							});
						}
						(function(marker, z) {
							google.maps.event.addListener(marker, 'click', function(e) {
								var contentString = "<a href='"+z.url+"'><ul style='list-style-type:none; margin:0; padding:0;'>" +
										"<li style='float:left; margin-right:1em;'><img src='"+z.photo_url+"'></li>" +
												"<li style='float:left'><h1>"+z.name+"</h1><p>"+z.full_address+"</p></li></ul></a>";
								infowindow.setContent(contentString);
								infowindow.open(map, marker);
							});
						})(marker, z);
					} 
				}).error(function(data, status, headers, config) {
					alert("failure");
				});
			}
			
			fillMap();
		}
	};
});

/*******************************   CHART PLOTTING FOR TWITTER SENTIMENT ************************/

/* implementation heavily influenced by http://bl.ocks.org/1166403 */
        
        // define dimensions of graph
        var m = [80, 80, 80, 80]; // margins
        var w = 1000 - m[1] - m[3]; // width
        var h = 400 - m[0] - m[2]; // height
        
        // create a simple data array that we'll plot with a line (this array represents only the Y values, X will just be the index location)
        var data = [3, 6, 2, 7, 5, 2, 0, 3, 8, 9, 2, 5, 9, 3, 6, 3, 6, 2, 7, 5, 2, 1, 3, 8, 9, 2, 5, 9, 2, 7];
 
        // X scale will fit all values from data[] within pixels 0-w
        var x = d3.scale.linear().domain([0, data.length]).range([0, w]);
        // Y scale will fit values from 0-10 within pixels h-0 (Note the inverted domain for the y-scale: bigger is up!)
        var y = d3.scale.linear().domain([0, 10]).range([h, 0]);
            // automatically determining max range can work something like this
            // var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);
 
        // create a line function that can convert data[] into x and y points
        var line = d3.svg.line()
            // assign the X function to plot our line as we wish
            .x(function(d,i) { 
                // verbose logging to show what's actually being done
                console.log('Plotting X value for data point: ' + d + ' using index: ' + i + ' to be at: ' + x(i) + ' using our xScale.');
                // return the X coordinate where we want to plot this datapoint
                return x(i); 
            })
            .y(function(d) { 
                // verbose logging to show what's actually being done
                console.log('Plotting Y value for data point: ' + d + ' to be at: ' + y(d) + " using our yScale.");
                // return the Y coordinate where we want to plot this datapoint
                return y(d); 
            })
 
            // Add an SVG element with the desired dimensions and margin.
            debugger;
            var graph = d3.select("#igraph").append("svg:svg")
                  .attr("width", w + m[1] + m[3])
                  .attr("height", h + m[0] + m[2])
                .append("svg:g")
                  .attr("transform", "translate(" + m[3] + "," + m[0] + ")");
 
            // create yAxis
            var xAxis = d3.svg.axis().scale(x).tickSize(-h).tickSubdivide(true);
            // Add the x-axis.
            graph.append("svg:g")
                  .attr("class", "x axis")
                  .attr("transform", "translate(0," + h + ")")
                  .call(xAxis);
 
 
            // create left yAxis
            var yAxisLeft = d3.svg.axis().scale(y).ticks(4).orient("left");
            // Add the y-axis to the left
            graph.append("svg:g")
                  .attr("class", "y axis")
                  .attr("transform", "translate(-25,0)")
                  .call(yAxisLeft);
            
            // Add the line by appending an svg:path element with the data line we created above
            // do this AFTER the axes above so that the line is above the tick-lines
            graph.append("svg:path").attr("d", line(data));



/*******************************   CHART PLOTTING FOR TWITTER SENTIMENT ************************/



















/*app.directive('pieplot', function($http) {
    return {
            restrict: 'EA',
            replace: true,
            template: '<div></div>',
            link: function(scope, element, attrs) {
                    console.log(element);
                    
                    makeChart = function() {
                            $http({
                                    method: 'GET',
                                    dataType: 'json',
                                    url: '/bigdata/v1/analytics/sentiment',
                                    headers: {
                                            'Content-Type': 'application/json'
                                    }
                            }).success(function(data, status, headers, config) {                                
                                      
                                    var width = 480,
                                        height = 300,
                                        radius = Math.min(width, height) / 2;

                                    var color = d3.scale.ordinal()
                                        .range(["yellow","green","red","orange"]);

                                    var arc = d3.svg.arc()
                                        .outerRadius(radius - 10)
                                        .innerRadius(0);

                                    var pie = d3.layout.pie()
                                        .sort(null)
                                        .value(function(d) { return d.value; });

                                    var svg = d3.select("#sentiment").append("svg")
                                        .attr("width", width)
                                        .attr("height", height)
                                      .append("g")
                                        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

                                    var g = svg.selectAll(".arc")
                                          .data(pie(data))
                                        .enter().append("g")
                                          .attr("class", "arc");

                                      g.append("path")
                                          .attr("d", arc) 
                                          .style("fill", function(d) { return color(d.data.sentiment); });

                                      
                            }).error(function(data, status, headers, config) {
                                    alert("failure");
                            });
                            
                    }
                    
                    makeChart();
            }
    };
});


app.directive('donutplot', function($http) {
    return {
            restrict: 'EA',
            replace: true,
            template: '<div></div>',
            link: function(scope, element, attrs) {
                    console.log(element);
                    
                    makeChart2 = function() {
                            $http({
                                    method: 'GET',
                                    dataType: 'json',
                                    url: '/bigdata/v1/analytics/sentiment',
                                    headers: {
                                            'Content-Type': 'application/json'
                                    }
                            }).success(function(data, status, headers, config) {                                
                                      
                                    var width = 480,
                                        height = 300,
                                        radius = Math.min(width, height) / 2;

                                    var color = d3.scale.ordinal()
                                        .range(["yellow","green","red","orange"]);

                                    var arc = d3.svg.arc()
                                .outerRadius(radius - 10)
                                .innerRadius(radius - 70);

                                    var pie = d3.layout.pie()
                                        .sort(null)
                                        .value(function(d) { return d.value; });

                                    var svg = d3.select("#sentimentdonut").append("svg")
                                        .attr("width", width)
                                        .attr("height", height)
                                      .append("g")
                                        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

                                    var g = svg.selectAll(".arc")
                                          .data(pie(data))
                                        .enter().append("g")
                                          .attr("class", "arc");

                                      g.append("path")
                                          .attr("d", arc) 
                                          .style("fill", function(d) { return color(d.data.sentiment); });

                                      g.append("text")
                                          .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
                                          .attr("dy", ".35em")
                                          .style("text-anchor", "middle")
                                          .text(function(d) { return d.data.value; });
                                      
                            }).error(function(data, status, headers, config) {
                                    alert("failure");
                            });
                            
                    }
                    
                    makeChart2();
            }
    };
});

*/
function MyCtrl($scope) {
    $scope.mapPin = "No pin set yet";
}
