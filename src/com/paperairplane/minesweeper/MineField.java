package com.paperairplane.minesweeper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * 
 * @author Xavier Yao
 * @see <a href="http://weibo.com/xavieryao">Xavier Yao's Sina Weibo </a>
 * 
 */
public class MineField {
	private int[] mMap;
	private int mSideLength;
	private int mMineAmount;
	private boolean[] mVisibilities;
	private boolean[] mIsEmpty;

	/**
	 * Constructor of class MineField
	 * 
	 * @param sideLength
	 *            side length of the mine field
	 * @param mines
	 *            the amount of the mines in the mine field
	 */
	public MineField(int sideLength, int mines) {
		if (sideLength * sideLength <= mines) {
			throw new IllegalArgumentException(
					"The amout of mines should not be greater than max size.");
		}
		int area = sideLength * sideLength;
		mMap = new int[area];
		mVisibilities = new boolean[area];
		mIsEmpty = new boolean[area];
		mSideLength = sideLength;
		mMineAmount = mines;
	}

	/**
	 * Method to check whether a block is visible
	 * 
	 * @param position
	 *            position of the block to be checked
	 * @return whether the block is visible
	 */
	public boolean isVisable(int position) {
		return mVisibilities[position];
	}

	/**
	 * Used to confirm if player has won.
	 * 
	 * @return whether all blocks are visible.
	 */
	public boolean won() {
		for (int i = 0; i < mVisibilities.length; i++) {
			if (!mVisibilities[i] && mMap[i] != BlockState.HAS_MINE) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method used for setting a block visible. Invoked when a block is clicked
	 * by player.
	 * 
	 * @param position
	 *            the position of the block to be made visible.
	 */
	public void makeVisible(int position) {
		mVisibilities[position] = true;
	}

	/**
	 * Check if the position is empty.
	 * 
	 * @param position
	 * @return
	 */
	public boolean isEmpty(int position) {
		return mIsEmpty[position];
	}

	/**
	 * The implementation of createMineField(). Block whose id is bannedId won't
	 * have a mine.
	 * 
	 * @param bannedId
	 */
	public void createMineField(int bannedId) {
		int maxId = mSideLength * mSideLength;
		System.out.println("--createMineField");
		addMinesToMap(maxId, bannedId);
		print();
		for (int i = 0; i < maxId; i++) {
			if (mMap[i] != BlockState.HAS_MINE) {
				mMap[i] = getNum(i, maxId);
			}
		}
	}

	private int getNum(int position, int maxId) {
		return match(position, maxId, BlockState.HAS_MINE).size();
	}

	public int getNum(int position) {
		return mMap[position];
	}

	private HashSet<Integer> match(int position, int maxId, int condition) {
		HashSet<Integer> matchedBlocks = new HashSet<Integer>();
		int cursor = position + 1;
		if (cursor < maxId) {
			if (mMap[cursor] == condition && (cursor % mSideLength != 0)
					&& !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
			}
		}
		cursor = position - 1;
		if (cursor >= 0) {
			if (mMap[cursor] == condition && ((cursor + 1) % mSideLength != 0)
					&& !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
			}
		}
		cursor = position - mSideLength - 1;
		if (cursor >= 0) {
			if (mMap[cursor] == condition && ((cursor + 1) % mSideLength != 0)
					&& !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
			}
		}
		cursor = position - mSideLength;
		if (cursor >= 0) {
			if (mMap[cursor] == condition && !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
			}
		}
		cursor = position - mSideLength + 1;
		if (cursor >= 0) {
			if (mMap[cursor] == condition && (cursor % mSideLength != 0)
					&& !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
			}
		}
		cursor = position + mSideLength;
		if (cursor < maxId) {
			if (mMap[cursor] == condition && !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
			}
		}
		cursor = position + mSideLength - 1;
		if (cursor < maxId) {
			if (mMap[cursor] == condition && ((cursor + 1) % mSideLength != 0)
					&& !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
				System.out.println("--The cursor found a mine at" + cursor);

			}
		}
		cursor = position + mSideLength + 1;
		if (cursor < maxId) {
			if (mMap[cursor] == condition && (cursor % mSideLength != 0)
					&& !mIsEmpty[cursor]) {
				matchedBlocks.add(cursor);
				System.out.println("--The cursor found a mine at" + cursor);
			}
		}
		System.out.println("--Current position is:" + position
				+ " The amount of matched blocks is:" + matchedBlocks.size());
		return matchedBlocks;
	}

	/**
	 * Method invoked to generate a random mine field.
	 */
	public void createMineField() {
		createMineField(-1);
	}

	private void addMinesToMap(int maxId, int bannedId) {
		ArrayList<Integer> mines = new ArrayList<Integer>();
		Random rnd = new Random();
		for (int i = 0; i < mMineAmount; i++) {
			int id = -2;
			System.out.println(!mines.contains(id) || id == bannedId);
			while (!mines.contains(id) || id == bannedId) {
				id = rnd.nextInt(maxId);
				mines.add(id);
			}
		}
		for (Integer i : mines) {
			mMap[i] = BlockState.HAS_MINE;
			System.out.println("--Mine: " + (i + 1));
		}
		System.out.println("--addMinesToMap Done.");
	}

	/**
	 * Get the amount of mines in the mine field.
	 * 
	 * @return amount
	 */
	public int getMineCount() {
		return mMineAmount;
	}

	/**
	 * Get the size of the mine field.
	 * 
	 * @return size
	 */
	public int getSize() {
		return mSideLength * mSideLength;
	}

	/**
	 * Print current mine field map.
	 */
	public void print() {
		System.out.println("--Here prints the minefield map:");
		int length = mMap.length;
		int times = length / mSideLength;
		for (int i = 0; i < times; i++) {
			for (int j = 0; j < mSideLength; j++) {
				int num = mMap[i * mSideLength + j];
				String str = (num == BlockState.HAS_MINE) ? "*" : Integer
						.toString(num);
				str = (mIsEmpty[i * mSideLength + j]) ? " " : str;
				System.out.print(str);
			}
			System.out.print("\n");
		}
	}

	public void remark(int position) {
		HashSet<Integer> nearbySafeBlocks = match(position, mSideLength
				* mSideLength, 0);
		while(nearbySafeBlocks.size()>0){
			markAsEmpty(nearbySafeBlocks);
			nearbySafeBlocks = search(nearbySafeBlocks,mSideLength*mSideLength);
		}
		}
	
	private HashSet<Integer> search(HashSet<Integer> nearbySafeBlocks,int maxId){
		HashSet<Integer> list = new HashSet<Integer>();
		for(int i:nearbySafeBlocks){
			list.addAll(match(i,mSideLength*mSideLength,0));
		}
		return list;
	}
	
	private void markAsEmpty(HashSet<Integer> list) {
		if (list.size() > 0) {
			for (int i : list) {
				mIsEmpty[i] = true;
				mVisibilities[i]=true;
			}
		}
	}
}
