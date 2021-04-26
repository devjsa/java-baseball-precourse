package com.woowahan;

import com.woowahan.vo.ResultVo;

import java.util.*;

import static com.woowahan.constants.MessageConstants.*;
import static com.woowahan.enums.EndType.END;
import static com.woowahan.enums.EndType.RETRY;
import static com.woowahan.enums.PlayType.*;

public class BaseballGame {

    static Scanner scanner;
    static Set<Integer> computerSet;

    public static void main(String[] args) {
        System.out.println(GREETING_MESSAGE);
        scanner = new Scanner(System.in);
        boolean isContinue = true;
        computerSet = getComputerSet();
        while (isContinue) {
            isContinue = playGame();
        }
        System.out.println(END_MESSAGE);
        scanner.close();
    }

    private static boolean playGame() {
        ResultVo resultVo = calculateScore(getValue());
        int strike = resultVo.getStrike();
        if (checkAllStrike(strike)) {
            return false;
        }
        printResult(resultVo.getBall(), strike);
        return true;
    }

    private static LinkedHashSet<Integer> getComputerSet() {
        LinkedHashSet<Integer> computerSet = new LinkedHashSet<>();
        Random random = new Random();
        while (computerSet.size() < 3) {
            computerSet.add(random.nextInt(10));
        }
        return computerSet;
    }

    private static void printResult(int ball, int strike) {
        if (ball + strike == 0) {
            System.out.println(NOTHING.getName());
            return;
        }
        String result = ball > 0 ? String.format("%d %s ", ball, BALL.getName()) : "";
        result += strike > 0 ? String.format("%d %s ", strike, STRIKE.getName()) : "";
        System.out.println(result);
    }

    private static boolean checkAllStrike(int strike) {
        if (strike != 3) {
            return false;
        }
        System.out.println(GAME_END_MESSAGE);
        System.out.println(NOTICE_MESSAGE);
        return isExitGame();
    }

    private static boolean isExitGame() {
        try {
            int input = scanner.nextInt();
            if (!RETRY.isEqualTo(input) && !END.isEqualTo(input)) {
                System.out.println(ERROR_MESSAGE);
                return isExitGame();
            }
            if (END.isEqualTo(input)) {
                return true;
            }
            computerSet = getComputerSet();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static ResultVo calculateScore(Set<Integer> playerSet) {
        Iterator<Integer> computerIterator = computerSet.iterator();
        Iterator<Integer> playerIterator = playerSet.iterator();
        ResultVo resultVo = new ResultVo();
        while (computerIterator.hasNext()) {
            getScore(computerIterator.next(), playerIterator.next(), resultVo);
        }
        return resultVo;
    }

    private static void getScore(int computerValue, int playerValue, ResultVo resultVo) {
        if (computerValue == playerValue) {
            resultVo.setStrike(resultVo.getStrike() + 1);
            return;
        }
        if (computerSet.contains(playerValue)) {
            resultVo.setBall(resultVo.getBall() + 1);
        }
    }

    private static Set<Integer> getValue() throws NumberFormatException {
        System.out.println(INPUT_REQUIRE_MESSAGE);
        Set<Integer> playerSet = new LinkedHashSet<>();
        String[] splitValue = scanner.nextLine().split("");
        if (validateInput(playerSet, splitValue)) {
            return getValueRetry();
        }
        if (playerSet.size() != 3) {
            return getValueRetry();
        }
        return playerSet;
    }

    private static boolean validateInput(Set<Integer> playerSet, String[] splitValue) {
        try {
            for (String num : splitValue) {
                playerSet.add(Integer.parseInt(num));
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private static Set<Integer> getValueRetry() {
        System.out.println(ERROR_MESSAGE);
        return getValue();
    }
}
