package org.papdt.minesweeper;

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
	private boolean[] mVisible;
	private boolean[] mIsEmpty;
	private boolean[] mFlagged;
	private long mSeed;

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
		return mVisible[position];
	}

	/**
	 * Used to confirm if player has won.
	 * 
	 * @return whether all blocks are visible.
	 */
	public boolean won() {
		for (int i = 0; i < mVisible.length; i++) {
			if (!mVisible[i] && mMap[i] != BlockState.HAS_MINE) {
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
		mVisible[position] = true;
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
	 * @param seed
	 */
	public void createMineField(int bannedId) {
		int maxId = mSideLength * mSideLength;
		mMap = new int[maxId];
		mVisible = new boolean[maxId];
		mIsEmpty = new boolean[maxId];
		mFlagged = new boolean[maxId];
		addMinesToMap(maxId, bannedId, mSeed);
		for (int i = 0; i < maxId; i++) {
			if (mMap[i] != BlockState.HAS_MINE) {
				mMap[i] = getNum(i, maxId);
			}
		}
	}

	public int getNum(int position) {
		return mMap[position];
	}

	/**
	 * Method invoked to generate a random mine field.
	 */
	public void createMineField() {
		mSeed = System.currentTimeMillis();
		createMineField(-1);
	}

	public void setSeed(long seed) {
		mSeed = seed;
	}

	/**
	 * Get the amount of mines in the mine field.
	 * 
	 * @return amount
	 */
	public int getRemainedMineCount() {
		int amount = 0;
		for (int i = 0; i < mFlagged.length; i++) {
			if (mFlagged[i]) {
				amount++;
			}
		}
		return mMineAmount - amount;
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
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(
				"--Here prints the minefield map:\n");
		int length = mMap.length;
		int times = length / mSideLength;
		for (int i = 0; i < times; i++) {
			for (int j = 0; j < mSideLength; j++) {
				int num = mMap[i * mSideLength + j];
				String str = (num == BlockState.HAS_MINE) ? "*" : Integer
						.toString(num);
				str = (mMap[i * mSideLength + j] == 0) ? " " : str;
				sb.append(str);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void flag(int position) {
		mFlagged[position] = !mFlagged[position];
	}

	public boolean flagged(int position) {
		return mFlagged[position];
	}

	public void remark(int position) {
		if (mMap[position] == 0) {
			remarkEmptyBlock(position, -1);
		} else {
			HashSet<Integer> nearbySafeBlocks = match(position, mSideLength
					* mSideLength, 0);
			while (nearbySafeBlocks.size() > 0) {
				markAsEmpty(nearbySafeBlocks);
				nearbySafeBlocks = search(nearbySafeBlocks, mSideLength
						* mSideLength);
			}
		}
	}

	private HashSet<Integer> match(int position, int maxId, int condition) {
		HashSet<Integer> matchedBlocks = new HashSet<Integer>();
		int cursor = position + 1;
		if (cursor < maxId && mMap[cursor] == condition
				&& (cursor % mSideLength != 0) && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - 1;
		if (cursor >= 0 && mMap[cursor] == condition
				&& ((cursor + 1) % mSideLength != 0) && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - mSideLength - 1;
		if (cursor >= 0 && mMap[cursor] == condition
				&& ((cursor + 1) % mSideLength != 0) && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - mSideLength;
		if (cursor >= 0 && mMap[cursor] == condition && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - mSideLength + 1;
		if (cursor >= 0 && mMap[cursor] == condition
				&& (cursor % mSideLength != 0) && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position + mSideLength;
		if (cursor < maxId && mMap[cursor] == condition && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position + mSideLength - 1;
		if (cursor < maxId && mMap[cursor] == condition
				&& ((cursor + 1) % mSideLength != 0) && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}
		cursor = position + mSideLength + 1;
		if (cursor < maxId && mMap[cursor] == condition
				&& (cursor % mSideLength != 0) && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}
		return matchedBlocks;
	}

	private int getNum(int position, int maxId) {
		return match(position, maxId, BlockState.HAS_MINE).size();
	}

	private void addMinesToMap(int maxId, int bannedId, long seed) {
		ArrayList<Integer> mines = new ArrayList<Integer>();
		Random rnd = new Random(seed);
		for (int i = 0, id = rnd.nextInt(maxId); i < mMineAmount; i++, id = rnd
				.nextInt(maxId)) {
			while (mines.contains(id) || id == bannedId) {
				id = rnd.nextInt(maxId);
			}
			mines.add(id);
		}
		for (Integer i : mines) {
			mMap[i] = BlockState.HAS_MINE;
		}
	}

	private HashSet<Integer> search(HashSet<Integer> nearbySafeBlocks, int maxId) {
		HashSet<Integer> list = new HashSet<Integer>();
		for (int i : nearbySafeBlocks) {
			list.addAll(match(i, mSideLength * mSideLength, 0));
		}
		return list;
	}

	private void markAsEmpty(HashSet<Integer> list) {
		if (list.size() > 0) {
			int maxId = mSideLength * mSideLength;
			for (int i : list) {
				mIsEmpty[i] = true;
				mVisible[i] = true;
				int cursor = i + 1;
				if (cursor < maxId && mMap[cursor] != BlockState.HAS_MINE
						&& (cursor % mSideLength != 0) && !mIsEmpty[cursor]
						&& !mFlagged[i]) {
					mVisible[cursor] = true;
				}
				cursor = i - 1;
				if (cursor >= 0 && mMap[cursor] != BlockState.HAS_MINE
						&& ((cursor + 1) % mSideLength != 0 && !mFlagged[i])
						&& !mIsEmpty[cursor]) {
					mVisible[cursor] = true;
				}

				cursor = i - mSideLength - 1;
				if (cursor >= 0 && mMap[cursor] != BlockState.HAS_MINE
						&& ((cursor + 1) % mSideLength != 0 && !mFlagged[i])
						&& !mIsEmpty[cursor]) {
					mVisible[cursor] = true;
				}

				cursor = i - mSideLength;
				if (cursor >= 0 && mMap[cursor] != BlockState.HAS_MINE
						&& !mIsEmpty[cursor] && !mFlagged[i]) {
					mVisible[cursor] = true;
				}

				cursor = i - mSideLength + 1;
				if (cursor >= 0 && mMap[cursor] != BlockState.HAS_MINE
						&& (cursor % mSideLength != 0) && !mIsEmpty[cursor]
						&& !mFlagged[i]) {
					mVisible[cursor] = true;
				}

				cursor = i + mSideLength;
				if (cursor < maxId && mMap[cursor] != BlockState.HAS_MINE
						&& !mIsEmpty[cursor] && !mFlagged[i]) {
					mVisible[cursor] = true;
				}

				cursor = i + mSideLength - 1;
				if (cursor < maxId && mMap[cursor] != BlockState.HAS_MINE
						&& ((cursor + 1) % mSideLength != 0 && !mFlagged[i])
						&& !mIsEmpty[cursor]) {
					mVisible[cursor] = true;
				}
				cursor = i + mSideLength + 1;
				if (cursor < maxId && mMap[cursor] != BlockState.HAS_MINE
						&& (cursor % mSideLength != 0) && !mIsEmpty[cursor]
						&& !mFlagged[i]) {
					mVisible[cursor] = true;
				}
			}
		}
	}
	
	private void remarkEmptyBlock(int position,int blocked){
		if(mMap[position] == 0){
			mIsEmpty[position] = true;
		}
		int maxId = mSideLength * mSideLength;
		HashSet<Integer> matchedBlocks = new HashSet<Integer>();
		int cursor = position + 1;
		if (cursor < maxId && (cursor % mSideLength != 0)
				&& !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - 1;
		if (cursor >= 0 && ((cursor + 1) % mSideLength != 0)
				&& !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - mSideLength - 1;
		if (cursor >= 0 && ((cursor + 1) % mSideLength != 0)
				&& !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - mSideLength;
		if (cursor >= 0 && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position - mSideLength + 1;
		if (cursor >= 0 && (cursor % mSideLength != 0) && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position + mSideLength;
		if (cursor < maxId && !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}

		cursor = position + mSideLength - 1;
		if (cursor < maxId && ((cursor + 1) % mSideLength != 0)
				&& !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}
		cursor = position + mSideLength + 1;
		if (cursor < maxId && (cursor % mSideLength != 0)
				&& !mIsEmpty[cursor]) {
			matchedBlocks.add(cursor);
		}
		
		for (int i : matchedBlocks){
			mVisible[i] = true;
		}
		
		for (int i : matchedBlocks){
			if (mMap[i] == 0 && i != blocked){
				remarkEmptyBlock(i,position);
			}
		}
	}

}
