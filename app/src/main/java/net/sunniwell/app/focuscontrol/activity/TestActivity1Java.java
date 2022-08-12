package net.sunniwell.app.focuscontrol.activity;

import androidx.annotation.NonNull;

import net.sunniwell.app.focuscontrol.base.BaseActivity;
import net.sunniwell.app.focuscontrol.databinding.ActivityTest1Binding;

/**
 * 测试活动1Java
 *
 * @author YSK
 * @date 2021-06-01
 */
public class TestActivity1Java extends BaseActivity<ActivityTest1Binding> {
    @NonNull
    @Override
    public ActivityTest1Binding bindView() {
        return ActivityTest1Binding.inflate(getLayoutInflater());
    }
}
