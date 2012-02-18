package vasoactive.decision.object;

/**
 * This class is used to hold intermediate decision states for glucose insulin
 * decision. This is because Drools has no mechanism for asserting fact
 * templates in rules files, forcing me to build some structure in Java.
 */
public class VasoactiveDecisionState {

	private boolean adult = false;
	private boolean pediatric = false;
	private boolean currentGlucoseInRange = false;
	private boolean currentGlucoseAboveRange = false;
	private boolean currentGlucoseBelowRange = false;
	private boolean mildHypoglycemia = false;
	private boolean moderateHypoglycemia = false;
	private boolean extremeHypoglycemia = false;
	private boolean insulinOn = false;
	private boolean insulinOff = false;
	private boolean insulinNegligible = false;
	private boolean needInsulinOff = false;
	private boolean needGlucoseBolus = false;
	private boolean recommendedInsulinOff = false;
	private boolean recommendedInsulinOn = false;
	private boolean recommendedInsulinUp = false;
	private boolean recommendedInsulinDown = false;
	private boolean recommendedGlucoseBolus = false;
	private boolean trimInsulinToMax = false;
	private boolean caloriesDecreased = false;
	private boolean caloriesIncreased = false;
	private boolean hypoglycemiaLast6Hours = false;
	private boolean hypoglycemiaLast12Hours = false;
	private boolean hypoglycemiaLast24Hours = false;
	private boolean glucoseBolusRecommendedLast6Hours = false;
	private boolean glucoseBolusRecommendedLast12Hours = false;
	private boolean glucoseBolusRecommendedLast24Hours = false;
	private boolean warning = false;

	public static final int SEVERE_HYPOTENSION = 0;
	public static final int MILD_HYPOTENSION = 1;
	public static final int ADEQUATE_BP = 2;
	public static final int HYPERTENSION = 3;
		
	private boolean needsPressor = false;
	private boolean needsCentralLine = false;
	private boolean recentAdjustment = false;
	private int		tempInteger = 0;
	private int		hypotensionLevel=999;		// error value
	private boolean stopLoop=false;
	
	private Integer warningSystolicBloodPressure = -1;
	private Integer minimumSystolicBloodPressure = -1;
	private Integer maximumSystolicBloodPressure = -1;
	
	public final void setWarning(final boolean warning) {
		this.warning = warning;
	}

	public final boolean isWarning() {
		return warning;
	}

	public VasoactiveDecisionState() {
		super();
	}

	public final boolean isAdult() {
		return adult;
	}

	public final void setAdult(final boolean adult) {
		this.adult = adult;
	}

	public final boolean isPediatric() {
		return pediatric;
	}

	public final void setPediatric(final boolean pediatric) {
		this.pediatric = pediatric;
	}

	public final boolean isCurrentGlucoseInRange() {
		return currentGlucoseInRange;
	}

	public final void setCurrentGlucoseInRange(final boolean currentGlucoseInRange) {
		this.currentGlucoseInRange = currentGlucoseInRange;
	}

	public final boolean isCurrentGlucoseAboveRange() {
		return currentGlucoseAboveRange;
	}

	public final void setCurrentGlucoseAboveRange(final boolean currentGlucoseAboveRange) {
		this.currentGlucoseAboveRange = currentGlucoseAboveRange;
	}

	public final boolean isCurrentGlucoseBelowRange() {
		return currentGlucoseBelowRange;
	}

	public final void setCurrentGlucoseBelowRange(final boolean currentGlucoseBelowRange) {
		this.currentGlucoseBelowRange = currentGlucoseBelowRange;
	}

	public final boolean isMildHypoglycemia() {
		return mildHypoglycemia;
	}

	public final void setMildHypoglycemia(final boolean mildHypoglycemia) {
		this.mildHypoglycemia = mildHypoglycemia;
	}

	public final boolean isModerateHypoglycemia() {
		return moderateHypoglycemia;
	}

	public final void setModerateHypoglycemia(final boolean moderateHypoglycemia) {
		this.moderateHypoglycemia = moderateHypoglycemia;
	}

	public final boolean isExtremeHypoglycemia() {
		return extremeHypoglycemia;
	}

	public final void setExtremeHypoglycemia(final boolean extremeHypoglycemia) {
		this.extremeHypoglycemia = extremeHypoglycemia;
	}

	public final boolean isInsulinOn() {
		return insulinOn;
	}

	public final void setInsulinOn(final boolean insulinOn) {
		this.insulinOn = insulinOn;
	}

	public final boolean isInsulinOff() {
		return insulinOff;
	}

	public final void setInsulinOff(final boolean insulinOff) {
		this.insulinOff = insulinOff;
	}

	public final boolean isInsulinNegligible() {
		return insulinNegligible;
	}

	public final void setInsulinNegligible(final boolean insulinNegligible) {
		this.insulinNegligible = insulinNegligible;
	}

	public final boolean isNeedInsulinOff() {
		return needInsulinOff;
	}

	public final void setNeedInsulinOff(final boolean needInsulinOff) {
		this.needInsulinOff = needInsulinOff;
	}

	public final boolean isNeedGlucoseBolus() {
		return needGlucoseBolus;
	}

	public final void setNeedGlucoseBolus(final boolean needGlucoseBolus) {
		this.needGlucoseBolus = needGlucoseBolus;
	}

	public final boolean isRecommendedInsulinOff() {
		return recommendedInsulinOff;
	}

	public final void setRecommendedInsulinOff(final boolean recommendedInsulinOff) {
		this.recommendedInsulinOff = recommendedInsulinOff;
	}

	public final boolean isRecommendedInsulinOn() {
		return recommendedInsulinOn;
	}

	public final void setRecommendedInsulinOn(final boolean recommendedInsulinOn) {
		this.recommendedInsulinOn = recommendedInsulinOn;
	}

	public final boolean isRecommendedInsulinUp() {
		return recommendedInsulinUp;
	}

	public final void setRecommendedInsulinUp(final boolean recommendedInsulinUp) {
		this.recommendedInsulinUp = recommendedInsulinUp;
	}

	public final boolean isRecommendedInsulinDown() {
		return recommendedInsulinDown;
	}

	public final void setRecommendedInsulinDown(final boolean recommendedInsulinDown) {
		this.recommendedInsulinDown = recommendedInsulinDown;
	}

	public final boolean isRecommendedGlucoseBolus() {
		return recommendedGlucoseBolus;
	}

	public final void setRecommendedGlucoseBolus(final boolean recommendedGlucoseBolus) {
		this.recommendedGlucoseBolus = recommendedGlucoseBolus;
	}

	public final boolean isTrimInsulinToMax() {
		return trimInsulinToMax;
	}

	public final void setTrimInsulinToMax(final boolean trimInsulinToMax) {
		this.trimInsulinToMax = trimInsulinToMax;
	}

	public final boolean isCaloriesDecreased() {
		return caloriesDecreased;
	}

	public final void setCaloriesDecreased(final boolean caloriesDecreased) {
		this.caloriesDecreased = caloriesDecreased;
	}

	public final boolean isCaloriesIncreased() {
		return caloriesIncreased;
	}

	public final void setCaloriesIncreased(final boolean caloriesIncreased) {
		this.caloriesIncreased = caloriesIncreased;
	}

	public final boolean isHypoglycemiaLast6Hours() {
		return hypoglycemiaLast6Hours;
	}

	public final void setHypoglycemiaLast6Hours(final boolean hypoglycemiaLast6Hours) {
		this.hypoglycemiaLast6Hours = hypoglycemiaLast6Hours;
	}

	public final boolean isHypoglycemiaLast12Hours() {
		return hypoglycemiaLast12Hours;
	}

	public final void setHypoglycemiaLast12Hours(final boolean hypoglycemiaLast12Hours) {
		this.hypoglycemiaLast12Hours = hypoglycemiaLast12Hours;
	}

	public final boolean isHypoglycemiaLast24Hours() {
		return hypoglycemiaLast24Hours;

	}

	public final void setHypoglycemiaLast24Hours(final boolean hypoglycemiaLast24Hours) {
		this.hypoglycemiaLast24Hours = hypoglycemiaLast24Hours;
	}

	public final boolean isGlucoseBolusRecommendedLast6Hours() {
		return glucoseBolusRecommendedLast6Hours;
	}

	public final void setGlucoseBolusRecommendedLast6Hours(
			final boolean glucoseBolusRecommendedLast6Hours) {
		this.glucoseBolusRecommendedLast6Hours = glucoseBolusRecommendedLast6Hours;
	}

	public final boolean isGlucoseBolusRecommendedLast12Hours() {
		return glucoseBolusRecommendedLast12Hours;
	}

	public final void setGlucoseBolusRecommendedLast12Hours(
			final boolean glucoseBolusRecommendedLast12Hours) {
		this.glucoseBolusRecommendedLast12Hours = glucoseBolusRecommendedLast12Hours;
	}

	public final boolean isGlucoseBolusRecommendedLast24Hours() {
		return glucoseBolusRecommendedLast24Hours;
	}

	public final void setGlucoseBolusRecommendedLast24Hours(
			final boolean glucoseBolusRecommendedLast24Hours) {
		this.glucoseBolusRecommendedLast24Hours = glucoseBolusRecommendedLast24Hours;
	}

	public boolean isNeedsPressor() {
		return needsPressor;
	}

	public void setNeedsPressor(boolean needsPressor) {
		this.needsPressor = needsPressor;
	}

	public boolean isNeedsCentralLine() {
		return needsCentralLine;
	}

	public void setNeedsCentralLine(boolean needsCentralLine) {
		this.needsCentralLine = needsCentralLine;
	}

	public boolean isRecentAdjustment() {
		return recentAdjustment;
	}

	public void setRecentAdjustment(boolean recentAdjustment) {
		this.recentAdjustment = recentAdjustment;
	}

	public int getTempInteger() {
		return tempInteger;
	}

	public void setTempInteger(int tempInteger) {
		this.tempInteger = tempInteger;
	}

	public int getHypotensionLevel() {
		return hypotensionLevel;
	}

	public void setHypotensionLevel(int hypotensionLevel) {
		this.hypotensionLevel = hypotensionLevel;
	}

	public boolean isStopLoop() {
		return stopLoop;
	}

	public void setStopLoop(boolean stopLoop) {
		this.stopLoop = stopLoop;
	}

	public Integer getWarningSystolicBloodPressure() {
		return warningSystolicBloodPressure;
	}

	public void setWarningSystolicBloodPressure(Integer warningSystolicBloodPressure) {
		this.warningSystolicBloodPressure = warningSystolicBloodPressure;
	}

	public Integer getMinimumSystolicBloodPressure() {
		return minimumSystolicBloodPressure;
	}

	public void setMinimumSystolicBloodPressure(Integer minimumSystolicBloodPressure) {
		this.minimumSystolicBloodPressure = minimumSystolicBloodPressure;
	}

	public Integer getMaximumSystolicBloodPressure() {
		return maximumSystolicBloodPressure;
	}

	public void setMaximumSystolicBloodPressure(Integer maximumSystolicBloodPressure) {
		this.maximumSystolicBloodPressure = maximumSystolicBloodPressure;
	}

}
