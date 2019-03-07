package com.coinninja.bindings;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Objects;

public class DerivationPath {

    private Integer purpose;
    private Integer coinType;
    private Integer account;
    private Integer change;
    private Integer index;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DerivationPath that = (DerivationPath) o;
        return Objects.equals(purpose, that.purpose) &&
                Objects.equals(coinType, that.coinType) &&
                Objects.equals(account, that.account) &&
                Objects.equals(change, that.change) &&
                Objects.equals(index, that.index);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(purpose, coinType, account, change, index);
    }

    public DerivationPath(String path) {
        String[] pathLevels = path.split("/");

        if (pathLevels.length == 1 && !pathLevels[0].isEmpty() || pathLevels.length > 6) {
            throw new IllegalArgumentException();
        }
        if (pathLevels.length > 1) {
            purpose = Integer.valueOf(pathLevels[1]);
        }
        if (pathLevels.length > 2) {
            coinType = Integer.valueOf(pathLevels[2]);
        }
        if (pathLevels.length > 3) {
            account = Integer.valueOf(pathLevels[3]);
        }
        if (pathLevels.length > 4) {
            change = Integer.valueOf(pathLevels[4]);
        }
        if (pathLevels.length > 5) {
            index = Integer.valueOf(pathLevels[5]);
        }
    }

    public DerivationPath(int purpose, int coinType, int account, int change, int index) {
        this.purpose = purpose;
        this.coinType = coinType;
        this.account = account;
        this.change = change;
        this.index = index;
    }

    public Integer getPurpose() {
        return purpose;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public Integer getAccount() {
        return account;
    }

    public Integer getChange() {
        return change;
    }

    public Integer getIndex() {
        return index;
    }

    public int[] toInts() {
        if (purpose == null || coinType == null || account == null || change == null || index == null) {
            throw new IllegalStateException("Cannot call toInts on invalid segwit derivationPath!");
        }

        return new int[]{purpose, coinType, account, change, index};
    }
}
