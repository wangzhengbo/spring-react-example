'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = React;

var _react2 = _interopRequireDefault(_react);

var _server = ReactDOMServer;

var _server2 = _interopRequireDefault(_server);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var CustomAttribute = function (_Component) {
    _inherits(CustomAttribute, _Component);

    function CustomAttribute() {
        _classCallCheck(this, CustomAttribute);

        return _possibleConstructorReturn(this, (CustomAttribute.__proto__ || Object.getPrototypeOf(CustomAttribute)).apply(this, arguments));
    }

    _createClass(CustomAttribute, [{
        key: 'render',
        value: function render() {
            return _react2.default.createElement(
                'div',
                { mycustomattribute: 'something', 'data-foo': '42', 'aria-label': 'Close', tabIndex: '2' },
                'Hello ',
                this.props.name,
                '.'
            );
        }
    }]);

    return CustomAttribute;
}(_react.Component);

console.log('React.version -> ', _react2.default.version);
console.log('renderToString -> ', _server2.default.renderToString(_react2.default.createElement(CustomAttribute, { name: 'React 16' })));
console.log('renderToStaticMarkup -> ', _server2.default.renderToStaticMarkup(_react2.default.createElement(CustomAttribute, { name: 'React 16' })));
//console.log('renderToNodeStream -> ', _server2.default.renderToNodeStream(_react2.default.createElement(CustomAttribute, { name: 'React 16' })));
//console.log('renderToStaticNodeStream -> ', _server2.default.renderToStaticNodeStream(_react2.default.createElement(CustomAttribute, { name: 'React 16' })));
