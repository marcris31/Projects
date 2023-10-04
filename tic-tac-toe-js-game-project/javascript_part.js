let turn = 1;

let matrix = [
  [-1, -1, -1],
  [-1, -1, -1],
  [-1, -1, -1]
];

let gameOver = false;
let gameWinner = -1;

function selectCell(row, col) {
  if (gameOver) {
    return;
  }
  const cells = document.getElementsByTagName('td'); //Array
  const index = 3 * row + col;
  if (!cells[index].innerHTML) {
    matrix[row][col] = turn ? 1 : 0;
    console.log(matrix);
    cells[index].innerHTML = turn ? 'X' : '0';
    turn = !turn;
    console.log(verifyWinner());
    const winner = verifyWinner();

    switch (winner) {
      case 1:
        gameOver = true;
        gameWinner = 'X';
        break;
      case 0:
        gameOver = true;
        gameWinner = 0;
        break;
      case 69:
        gameOver = true;
        gameWinner = 'remiza';
        break;
    }

    if (winner !== -1) {
      document.getElementById('winner').innerHTML = `${gameWinner} won!`;
    }
    if(winner == 69){
    	document.getElementById('winner').innerHTML = `remiza!`;
    }
  }
}

const verifyWinner = () => {
  //Verify lines
  for (let i = 0; i < 3; i++) {
    let winner = matrix[i][0];
    for (let j = 0; j < 3; j++) {
      if (matrix[i][j] !== winner) {
        winner = -1;
      }
    }
    if (winner !== -1) {
      return winner;
    }
  }

  //Verify columns
  for (let j = 0; j < 3; j++) {
    let winner = matrix[0][j];
    for (let i = 0; i < 3; i++) {
      if (matrix[i][j] !== winner) {
        winner = -1;
      }
    }
    if (winner !== -1) {
      return winner;
    }
  }

  //Verify main diagonal
  let winner = matrix[0][0];
  for (let i = 0; i < 3; i++) {
    if (winner !== matrix[i][i]) {
      winner = -1;
    }
  }
  if (winner !== -1) {
    return winner;
  }

  //Verify secondary diagonal
  winner = matrix[0][2];
  for (let i = 0; i < 3; i++) {
    let j = 3 - i - 1;
    if (matrix[i][j] !== winner) {
      winner = -1;
    }
  }
  if (winner !== -1) {
    return winner;
  }

  //Verify for draw
  let draw = 1;
  for (let i = 0; i < 3; i++) {
    for (let j = 0; j < 3; j++) {
      if (matrix[i][j] === -1) {
        draw = 0;
      }
    }
  }

  if (draw) {
    return 69;
  }
  return -1;
}
