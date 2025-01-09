package umc.kkijuk.server.detail.domain;

import umc.kkijuk.server.career.domain.*;

public enum CareerType {
    ACTIVITY(1,"대외활동", Activity.class),
    PROJECT(2,"프로젝트", Project.class),
    EDU(3,"교육", EduCareer.class),
    EMP(4,"경력", Employment.class),
    CIRCLE(5,"동아리", Circle.class),
    COM(6,"대회", Competition.class),
    ETC(7,"기타", CareerEtc.class);

    private final int id;
    private final String description;
    private final Class<? extends BaseCareer> career;

    CareerType(int id, String description, Class<? extends BaseCareer> career) {
        this.id = id;
        this.description = description;
        this.career = career;
    }

    public String getDescription() {
        return description;
    }
    public int getId() {return id;}
    public Class<? extends BaseCareer> getCareer() {
        return career;
    }
    public static CareerType fromClass(BaseCareer careerClass){
        for (CareerType type : values()) {
            if (type.getCareer().equals(careerClass.getClass())) {
                return type;
            }
        }
        throw new IllegalArgumentException("지원하는 활동이 아닙니다 : " + careerClass.getClass());

    }
}
