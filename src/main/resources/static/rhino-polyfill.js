var global = this;

var out = java.lang.System.out;
var err = java.lang.System.err;
var log = function(out) {
	for(var i = 0, length = arguments.length; i < length; i++) {
		if (i > 0) {
			this.print(' ');
		}
		this.print(arguments[i]);
	}
	this.println();
};

var console = {};
console.debug = function() {
	log.apply(out, arguments);
};
console.warn = function() {
	log.apply(err, arguments);
};
console.log = function() {
	log.apply(out, arguments);
};