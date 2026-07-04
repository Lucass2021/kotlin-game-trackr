package com.lucasdias.gametrackr.core.ui.icon

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Bell
import com.adamglin.phosphoricons.regular.CaretLeft
import com.adamglin.phosphoricons.regular.CaretRight
import com.adamglin.phosphoricons.regular.Check
import com.adamglin.phosphoricons.regular.CheckCircle
import com.adamglin.phosphoricons.regular.Compass
import com.adamglin.phosphoricons.regular.Envelope
import com.adamglin.phosphoricons.regular.Eye
import com.adamglin.phosphoricons.regular.EyeSlash
import com.adamglin.phosphoricons.regular.GameController
import com.adamglin.phosphoricons.regular.Gear
import com.adamglin.phosphoricons.regular.House
import com.adamglin.phosphoricons.regular.Info
import com.adamglin.phosphoricons.regular.ListBullets
import com.adamglin.phosphoricons.regular.MagnifyingGlass
import com.adamglin.phosphoricons.regular.Medal
import com.adamglin.phosphoricons.regular.Question
import com.adamglin.phosphoricons.regular.ShieldCheck
import com.adamglin.phosphoricons.regular.SquaresFour
import com.adamglin.phosphoricons.regular.Stack
import com.adamglin.phosphoricons.regular.User
import com.adamglin.phosphoricons.regular.UserCircle
import com.adamglin.phosphoricons.regular.UsersThree
import com.adamglin.phosphoricons.fill.Bell as BellFill
import com.adamglin.phosphoricons.fill.CaretLeft as CaretLeftFill
import com.adamglin.phosphoricons.fill.CaretRight as CaretRightFill
import com.adamglin.phosphoricons.fill.Check as CheckFill
import com.adamglin.phosphoricons.fill.CheckCircle as CheckCircleFill
import com.adamglin.phosphoricons.fill.Compass as CompassFill
import com.adamglin.phosphoricons.fill.Envelope as EnvelopeFill
import com.adamglin.phosphoricons.fill.Eye as EyeFill
import com.adamglin.phosphoricons.fill.EyeSlash as EyeSlashFill
import com.adamglin.phosphoricons.fill.GameController as GameControllerFill
import com.adamglin.phosphoricons.fill.Gear as GearFill
import com.adamglin.phosphoricons.fill.House as HouseFill
import com.adamglin.phosphoricons.fill.Info as InfoFill
import com.adamglin.phosphoricons.fill.ListBullets as ListBulletsFill
import com.adamglin.phosphoricons.fill.MagnifyingGlass as MagnifyingGlassFill
import com.adamglin.phosphoricons.fill.Medal as MedalFill
import com.adamglin.phosphoricons.fill.Question as QuestionFill
import com.adamglin.phosphoricons.fill.ShieldCheck as ShieldCheckFill
import com.adamglin.phosphoricons.fill.SquaresFour as SquaresFourFill
import com.adamglin.phosphoricons.fill.Stack as StackFill
import com.adamglin.phosphoricons.fill.User as UserFill
import com.adamglin.phosphoricons.fill.UserCircle as UserCircleFill
import com.adamglin.phosphoricons.fill.UsersThree as UsersThreeFill

enum class AppIcon(
    val regular: ImageVector,
    val filled: ImageVector,
) {
    HOME(PhosphorIcons.Regular.House, PhosphorIcons.Fill.HouseFill),
    LIBRARY(PhosphorIcons.Regular.Stack, PhosphorIcons.Fill.StackFill),
    COMMUNITY(PhosphorIcons.Regular.UsersThree, PhosphorIcons.Fill.UsersThreeFill),
    PROFILE(PhosphorIcons.Regular.User, PhosphorIcons.Fill.UserFill),
    DISCOVER(PhosphorIcons.Regular.Compass, PhosphorIcons.Fill.CompassFill),
    NOTIFICATIONS(PhosphorIcons.Regular.Bell, PhosphorIcons.Fill.BellFill),
    SEARCH(PhosphorIcons.Regular.MagnifyingGlass, PhosphorIcons.Fill.MagnifyingGlassFill),
    SETTINGS(PhosphorIcons.Regular.Gear, PhosphorIcons.Fill.GearFill),
    ENVELOPE(PhosphorIcons.Regular.Envelope, PhosphorIcons.Fill.EnvelopeFill),
    EYE(PhosphorIcons.Regular.Eye, PhosphorIcons.Fill.EyeFill),
    EYE_SLASH(PhosphorIcons.Regular.EyeSlash, PhosphorIcons.Fill.EyeSlashFill),
    CHECK(PhosphorIcons.Regular.Check, PhosphorIcons.Fill.CheckFill),
    SUCCESS(PhosphorIcons.Regular.CheckCircle, PhosphorIcons.Fill.CheckCircleFill),
    SHIELD_CHECK(PhosphorIcons.Regular.ShieldCheck, PhosphorIcons.Fill.ShieldCheckFill),
    BACK(PhosphorIcons.Regular.CaretLeft, PhosphorIcons.Fill.CaretLeftFill),
    FORWARD(PhosphorIcons.Regular.CaretRight, PhosphorIcons.Fill.CaretRightFill),
    BRAND(PhosphorIcons.Regular.GameController, PhosphorIcons.Fill.GameControllerFill),
    AVATAR(PhosphorIcons.Regular.UserCircle, PhosphorIcons.Fill.UserCircleFill),
    EDIT_PROFILE(PhosphorIcons.Regular.UserCircle, PhosphorIcons.Fill.UserCircleFill),
    MEDAL(PhosphorIcons.Regular.Medal, PhosphorIcons.Fill.MedalFill),
    INFO(PhosphorIcons.Regular.Info, PhosphorIcons.Fill.InfoFill),
    GRID(PhosphorIcons.Regular.SquaresFour, PhosphorIcons.Fill.SquaresFourFill),
    LIST(PhosphorIcons.Regular.ListBullets, PhosphorIcons.Fill.ListBulletsFill),
    HELP(PhosphorIcons.Regular.Question, PhosphorIcons.Fill.QuestionFill),
    ;

    fun image(filled: Boolean = false): ImageVector = if (filled) this.filled else regular
}
