const defaultRValue = 1;

const board = JXG.JSXGraph.initBoard("jxgbox", {
    boundingbox: [-defaultRValue * 2, defaultRValue * 2, defaultRValue * 2, -defaultRValue * 2],
    axis:true
});

const pointRadius = 1;

const points = {
    xMinusHalfR: board.create("point", [-defaultRValue/2, 0], {size:pointRadius, name:"R/2"}),
    xPlusHalfR: board.create("point", [defaultRValue/2, 0], {size:pointRadius, name:"R/2"}),
    yMinusHalfR: board.create("point", [0, -defaultRValue/2], {size:pointRadius, name:"R/2"}),
    yPlusHalfR: board.create("point", [0, defaultRValue/2], {size:pointRadius, name:"R/2"}),

    xMinusR: board.create("point", [-defaultRValue, 0], {size:pointRadius, name:"R"}),
    xPlusR: board.create("point", [defaultRValue,0], {size:pointRadius, name:"R"}),
    yMinusR: board.create("point", [0, -defaultRValue], {size:pointRadius, name:"R"}),
    yPlusR: board.create("point", [0, defaultRValue], {size:pointRadius, name:"R"}),

    center: board.create("point", [0, 0], {size:pointRadius, name:""}),

    enumerateMarkings: () => {
        let result = [
            points.xMinusHalfR, 
            points.xPlusHalfR,
            points.yMinusHalfR,
            points.yPlusHalfR,
            points.xMinusR,
            points.xPlusR,
            points.yMinusR,
            points.yPlusR
        ]

        return result;
    },

    addedPointsMap: {}
}

var currentRValue = defaultRValue;
const changeBoardR = (value) => {
    board.suspendUpdate();
    
    setVisiblePointsByR(currentRValue, false);
    setVisiblePointsByR(value, true);

    scalePointsBy(points.enumerateMarkings(), value/currentRValue)    
    
    currentRValue = value;
    remakeGraphs(topBatmanFunction, bottomBatmanFunction, currentRValue);

    board.unsuspendUpdate();
}

const setVisiblePointsByR = (r, visible) => {
    if (points.addedPointsMap[r] === undefined) {
        return;
    }
    points.addedPointsMap[r].forEach(point => {
        point.setAttribute({visible: visible})
    });
}

const scalePointsBy = (points, scaleFactor) => {
    tr = board.create("transform", [scaleFactor, scaleFactor], {type: "scale"});
    points.forEach((point) => tr.applyOnce(point))
}

/* https://www.desmos.com/calculator/umrkccha3l */
const topBatman = (xValue,r) =>  {
    let x = Math.abs(xValue);
    let y = 0;
    if(x<(r)/(14)) {
        y=(9*r)/(28)
    }
    else if (x<(3*r)/(28)) {
        y=3*x+(3*r)/(28)
    }
    else if (x<r/7) {
        y=((9*r)/(7))-8*x
    }
    else if (x<(r*3)/(7)) {
        y=((3*r)/14)-x/2-((6*Math.sqrt(10))/14)*(Math.sqrt(3*(r/7)**2 - x**2 + (2*x*r)/(7)) - (2*r)/(7))
    }
    else if(x<r) {
        y=((r*3)/(7)) * Math.sqrt(-(((x)/(r))**2)+1)
    } 
    return y;
}

const bottomBatman = (xValue,r) => {
    let x = Math.abs(xValue);
    let y = 0;

    if (x<(r*4)/(7)) {
        y=x/2 - ((r*(3*Math.sqrt(33)-7))/(784))*((7*x)/(r))**2 + (r/7) * Math.sqrt(1-(Math.abs((7*x/r)-2)-1)**2)-(r*3)/7
    }
    
    else if (x<r) {
        y=-((r*3)/7)*Math.sqrt(-((x/r)**2) + 1)
    }
    
    return y;
}

const topBatmanFunction = (x) => topBatman(x, currentRValue)
const bottomBatmanFunction = (x) => bottomBatman(x, currentRValue)

var topGraph = board.create("functiongraph",
    [topBatmanFunction, -currentRValue, currentRValue]
 );

var bottomGraph = board.create("functiongraph",
    [bottomBatmanFunction, -currentRValue, currentRValue]
);

const integralOptions = {
    label: {
      visible: false
    },
    curveLeft: {
      visible: false
    },
    curveRight: {
      visible: false
    },
    baseRight: {
        visible: false
    },
    baseLeft: {
        visible: false
    },
    fillColor: "blue",
}

var i1 = board.create("integral", [
    [-currentRValue, currentRValue], topGraph
  ], integralOptions);


var i2 = board.create("integral", [
    [-currentRValue, currentRValue], bottomGraph
  ], integralOptions);

const remakeGraphs = (topFunction, bottomFunction, rValue) => {
    board.removeObject(i1)
    board.removeObject(i2)
    board.removeObject(topGraph)
    board.removeObject(bottomGraph)

    topGraph = board.create("functiongraph",
        [topFunction, -rValue, rValue]
     );
    
    bottomGraph = board.create("functiongraph",
        [bottomFunction, -rValue, rValue]
    );

    i1 = board.create("integral", [
        [-rValue, rValue], topGraph
      ], integralOptions);
    
    
    i2 = board.create("integral", [
        [-rValue, rValue], bottomGraph
      ], integralOptions);
}

const createNewHitPoint = (x,y,r, options) => {

    let newPoint = board.create("point", [x,y], options);

    if (r != currentRValue) {
        newPoint.setAttribute({visible: false});
    }

    if (points.addedPointsMap[r] === undefined) {
        points.addedPointsMap[r] = []
    }

    let oldPoint = isPointExistsAt(x,y,r);

    if (oldPoint != null) {
        board.removeObject(oldPoint);
    }

    points.addedPointsMap[r].push(newPoint);
}

const isPointExistsAt = (x,y,r) => {
    let oldPoint = null;

    points.addedPointsMap[r].forEach(point => {
        if (point.X().toFixed(2) == (+x).toFixed(2) && point.Y().toFixed(2) == (+y).toFixed(2)) {
            oldPoint = point;
        }
    })

    return oldPoint;
}



board.on("down", (e) => {
    var pos = board.getMousePosition(e, 0);
    let coords = new JXG.Coords(JXG.COORDS_BY_SCREEN, pos, board);

    let x = coords.usrCoords[1]
    let y = coords.usrCoords[2]

    sendRequest(x.toFixed(1),y,currentRValue);
});
